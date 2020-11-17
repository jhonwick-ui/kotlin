/*
 * Copyright 2010-2020 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.idea.fir.low.level.api

import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.declarations.FirResolvePhase
import org.jetbrains.kotlin.fir.resolve.ScopeSession
import org.jetbrains.kotlin.fir.resolve.transformers.FirTransformerBasedResolveProcessor
import org.jetbrains.kotlin.fir.resolve.transformers.createTransformerBasedProcessorByPhase


class FirTransformerProvider {
    fun getTransformerForPhase(session: FirSession, phase: FirResolvePhase): FirTransformerBasedResolveProcessor {
        val scopesSession = getScopeSession(session)
        return phase.createTransformerBasedProcessorByPhase(session, scopesSession)
    }

    fun getScopeSession(session: FirSession): ScopeSession = ScopeSession()
}