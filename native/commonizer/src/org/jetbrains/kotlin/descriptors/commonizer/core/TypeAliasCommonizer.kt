/*
 * Copyright 2010-2019 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.descriptors.commonizer.core

import org.jetbrains.kotlin.descriptors.ClassKind
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.descriptors.commonizer.cir.*
import org.jetbrains.kotlin.descriptors.commonizer.cir.factory.CirClassFactory
import org.jetbrains.kotlin.descriptors.commonizer.cir.factory.CirTypeAliasFactory
import org.jetbrains.kotlin.descriptors.commonizer.mergedtree.CirClassifiersCache
import org.jetbrains.kotlin.descriptors.commonizer.utils.isUnderStandardKotlinPackages
import org.jetbrains.kotlin.name.Name

/**
 * Primary (optimistic) branch:
 * - Make sure that all TAs expand to the same type, so the resulting TA can be short-circuited and lifted up into "common" fragment.
 *
 * Secondary (less optimistic) branch:
 * - Make sure that all TAs are identical, so the resulting TA can be lifted up into "common" fragment.
 *
 * Tertiary (backup) branch:
 * - Produce an "expect class" for "common" fragment and the corresponding "actual typealias" declarations for each platform fragment.
 */
class TypeAliasCommonizer(cache: CirClassifiersCache) : AbstractStandardCommonizer<CirTypeAlias, CirClassifier>() {
    private val primary = TypeAliasShortCircuitingCommonizer(cache)
    private val secondary = TypeAliasLiftingUpCommonizer(cache)
    private val tertiary = TypeAliasExpectClassCommonizer()

    override fun commonizationResult(): CirClassifier = primary.resultOrNull ?: secondary.resultOrNull ?: tertiary.result

    override fun initialize(first: CirTypeAlias) = Unit

    override fun doCommonizeWith(next: CirTypeAlias): Boolean {
        val primaryResult = primary.commonizeWith(next)
        val secondaryResult = secondary.commonizeWith(next)
        val tertiaryResult = tertiary.commonizeWith(next)

        // Note: don't call commonizeWith() functions in return statement to avoid short-circuiting!
        return primaryResult || secondaryResult || tertiaryResult
    }
}

private class TypeAliasShortCircuitingCommonizer(cache: CirClassifiersCache) : AbstractStandardCommonizer<CirTypeAlias, CirTypeAlias>() {
    private lateinit var name: Name
    private val typeParameters = TypeParameterListCommonizer(cache)
    private lateinit var underlyingType: CirClassOrTypeAliasType
    private val expandedType = TypeCommonizer(cache)
    private val visibility = VisibilityCommonizer.lowering()

    override fun commonizationResult() = CirTypeAliasFactory.create(
        annotations = emptyList(),
        name = name,
        typeParameters = typeParameters.result,
        visibility = visibility.result,
        underlyingType = computeCommonizedUnderlyingType(underlyingType),
        expandedType = expandedType.result as CirClassType
    )

    val resultOrNull: CirTypeAlias?
        get() = if (hasResult) commonizationResult() else null

    override fun initialize(first: CirTypeAlias) {
        name = first.name
        underlyingType = first.underlyingType
    }

    override fun doCommonizeWith(next: CirTypeAlias) =
        typeParameters.commonizeWith(next.typeParameters)
                && expandedType.commonizeWith(next.expandedType)
                && visibility.commonizeWith(next)

    private tailrec fun computeCommonizedUnderlyingType(underlyingType: CirClassOrTypeAliasType): CirClassOrTypeAliasType {
        return when (underlyingType) {
            is CirClassType -> underlyingType
            is CirTypeAliasType -> if (underlyingType.classifierId.packageFqName.isUnderStandardKotlinPackages)
                underlyingType
            else
                computeCommonizedUnderlyingType(underlyingType.underlyingType)
        }
    }
}

private class TypeAliasLiftingUpCommonizer(cache: CirClassifiersCache) : AbstractStandardCommonizer<CirTypeAlias, CirTypeAlias>() {
    private lateinit var name: Name
    private val typeParameters = TypeParameterListCommonizer(cache)
    private val underlyingType = TypeCommonizer(cache)
    private val visibility = VisibilityCommonizer.lowering()

    override fun commonizationResult(): CirTypeAlias {
        val underlyingType = underlyingType.result as CirClassOrTypeAliasType

        return CirTypeAliasFactory.create(
            annotations = emptyList(),
            name = name,
            typeParameters = typeParameters.result,
            visibility = visibility.result,
            underlyingType = underlyingType,
            expandedType = computeCommonizedExpandedType(underlyingType)
        )
    }

    val resultOrNull: CirTypeAlias?
        get() = if (hasResult) commonizationResult() else null

    override fun initialize(first: CirTypeAlias) {
        name = first.name
    }

    override fun doCommonizeWith(next: CirTypeAlias) =
        typeParameters.commonizeWith(next.typeParameters)
                && underlyingType.commonizeWith(next.underlyingType)
                && visibility.commonizeWith(next)

    private tailrec fun computeCommonizedExpandedType(underlyingType: CirClassOrTypeAliasType): CirClassType {
        return when (underlyingType) {
            is CirClassType -> underlyingType
            is CirTypeAliasType -> computeCommonizedExpandedType(underlyingType.underlyingType)
        }
    }
}

private class TypeAliasExpectClassCommonizer : AbstractStandardCommonizer<CirTypeAlias, CirClass>() {
    private lateinit var name: Name
    private val classVisibility = VisibilityCommonizer.equalizing()

    override fun commonizationResult(): CirClass = CirClassFactory.create(
        annotations = emptyList(),
        name = name,
        typeParameters = emptyList(),
        visibility = classVisibility.result,
        modality = Modality.FINAL,
        kind = ClassKind.CLASS,
        companion = null,
        isCompanion = false,
        isData = false,
        isInline = false,
        isInner = false,
        isExternal = false
    )

    override fun initialize(first: CirTypeAlias) {
        name = first.name
    }

    override fun doCommonizeWith(next: CirTypeAlias): Boolean {
        if (next.typeParameters.isNotEmpty())
            return false // TAs with declared type parameters can't be commonized

        val underlyingType = next.underlyingType as? CirClassType ?: return false // right-hand side could have only class
        return hasNoArguments(underlyingType) // TAs with functional types or types with arguments at the right-hand side can't be commonized
                && classVisibility.commonizeWith(underlyingType) // the visibilities of the right-hand classes should be equal
    }

    private tailrec fun hasNoArguments(type: CirClassType?): Boolean =
        when {
            type == null -> true
            type.arguments.isNotEmpty() -> false
            else -> hasNoArguments(type.outerType)
        }
}
