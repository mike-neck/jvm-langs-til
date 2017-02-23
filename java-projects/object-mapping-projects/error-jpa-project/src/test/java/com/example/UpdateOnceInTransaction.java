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

import com.example.entity.Customer;
import com.example.entity.WorkHistories;
import com.example.repository.CustomerRepository;
import com.example.repository.WorkHistoriesRepository;
import com.example.work.Run;
import com.example.work.Start;
import com.google.inject.Injector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

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
    }

    @Test
    void updateOnlyOnce(Injector injector) throws Throwable {
        final Start start = injector.getInstance(Start.class);
        try (final Run run = start.start()) {
            run.transaction(em -> {
                final CustomerRepository repository = injector.getInstance(CustomerRepository.class);
                final Customer customer = repository.findCustomerForUpdate(id).orElseThrow(RuntimeException::new);
                customer.setName("test-update");
                final Customer c = repository.update(customer);
                System.out.println("Update : " + c);
            });
            run.transaction(em -> {
                final CustomerRepository cr = injector.getInstance(CustomerRepository.class);
                final WorkHistoriesRepository whr = injector.getInstance(WorkHistoriesRepository.class);
                final Customer customer = cr.findCustomer(id).orElseThrow(RuntimeException::new);
                System.out.println("Update Confirm : " + customer);
                final WorkHistories wh = new WorkHistories("test", "test-update");
                whr.create(wh);
                final Customer c = cr.findCustomer(id).orElseThrow(RuntimeException::new);
                System.out.println("After commit of another entity : " + c);
            });
        }
        final CustomerRepository cr = injector.getInstance(CustomerRepository.class);
        final Customer c = cr.findCustomer(id).orElseThrow(RuntimeException::new);
        System.out.println("After unit of work : " + c);
    }
}
