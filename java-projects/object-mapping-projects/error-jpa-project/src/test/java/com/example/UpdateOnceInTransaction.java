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

import com.example.data.Tuple;
import com.example.entity.Customer;
import com.example.entity.WorkHistories;
import com.example.function.Functions;
import com.example.repository.CustomerRepository;
import com.example.repository.WorkHistoriesRepository;
import com.example.work.Run;
import com.example.work.Start;
import com.google.inject.Injector;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith({JpaInitializer.class})
public class UpdateOnceInTransaction {

    private Long id;

    @BeforeEach
    void setup(Injector injector) {
        final CustomerRepository repository = injector.getInstance(CustomerRepository.class);
        final EntityTransaction tx = injector.getInstance(EntityManager.class).getTransaction();
        tx.begin();
        final Customer c = new Customer("test");
        final Customer customer = repository.create(c);
        id = customer.getId();
        tx.commit();
        System.out.println("Before : " + customer);
        assertEquals(1, customer.getVersion());
    }

    @Test
    void updateOnlyOnce(Injector injector) throws Throwable {
        final Start start = injector.getInstance(Start.class);
        try (final Run run = start.start()) {
            run.transaction((em, nothing) -> {
                final CustomerRepository repository = injector.getInstance(CustomerRepository.class);
                final Customer customer = repository.findCustomerForUpdate(id).orElseThrow(RuntimeException::new);

                customer.setName("test-update");
                final Customer c = repository.update(customer);

                System.out.println("Update : " + c);
                assertEquals(2, c.getVersion());

                return new Tuple<>(repository, id);
            }).transaction((em, t) -> {
                final Customer customer = t.getLeft().findCustomer(t.getRight()).orElseThrow(RuntimeException::new);
                System.out.println("Update Confirm : " + customer);
                assertEquals(2, customer.getVersion());

                final WorkHistories wh = new WorkHistories("test", "test-update");
                injector.getInstance(WorkHistoriesRepository.class).create(wh);

                return t.getLeft().findCustomer(t.getRight()).orElseThrow(RuntimeException::new);
            }).andFinally(c -> {
                System.out.println("After commit of another entity : " + c);
                assertEquals(2, c.getVersion());
            });
        }
        final CustomerRepository cr = injector.getInstance(CustomerRepository.class);
        final Customer c = cr.findCustomer(id).orElseThrow(RuntimeException::new);
        System.out.println("After unit of work : " + c);
    }
}
