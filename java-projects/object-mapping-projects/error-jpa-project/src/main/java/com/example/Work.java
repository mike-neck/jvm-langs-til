/*
 * Copyright 2017 Shinya Mochida
 * 
 * Licensed under the Apache License,Version2.0(the"License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,software
 * Distributed under the License is distributed on an"AS IS"BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example;

import com.example.function.Functions;
import com.example.work.Run;
import com.example.work.Start;
import com.example.work.Tx;
import com.google.inject.persist.Transactional;
import com.google.inject.persist.UnitOfWork;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.Optional;

public class Work implements Start, Run {

    private final UnitOfWork unitOfWork;
    private final EntityManager em;

    @Inject
    public Work(UnitOfWork unitOfWork, EntityManager em) {
        this.unitOfWork = unitOfWork;
        this.em = em;
    }

    @Override
    public Run start() {
//        unitOfWork.begin();
        return this;
    }

    @Override
    public void close() throws Exception {
        unitOfWork.end();
    }

    @Transactional
    @Override
    public <R> Tx<R> transaction(Functions.ExBiFunction<? super EntityManager, ? super Void, ? extends R> script)
            throws Throwable {
        @SuppressWarnings("ConstantConditions")
        final R value = script.apply(em, null);
        return new Tx.Impl<>(em, value);
    }

    @NotNull
    @Override
    public Optional<Void> finish() {
        return Optional.empty();
    }
}
