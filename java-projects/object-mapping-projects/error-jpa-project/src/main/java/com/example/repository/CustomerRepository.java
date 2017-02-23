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
package com.example.repository;

import com.example.entity.Customer;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

public class CustomerRepository {

    private final EntityManager em;
    private final ZoneId zone;

    @Inject
    public CustomerRepository(EntityManager em, ZoneId zone) {
        this.em = em;
        this.zone = zone;
    }

    @NotNull
    public Customer create(Customer customer) {
        final LocalDateTime now = currentTime();
        customer.setCreated(now);
        customer.setUpdated(now);
        em.persist(customer);
        em.flush();
        return customer;
    }

    @NotNull
    public Customer update(Customer customer) {
        customer.setUpdated(currentTime());
        em.persist(customer);
        em.flush();
        return customer;
    }

    @NotNull
    private LocalDateTime currentTime() {
        return LocalDateTime.now(zone);
    }

    @NotNull
    public Optional<Customer> findCustomerForUpdate(Long id) {
        return em.createQuery("select c from Customer c where c.id = :id", Customer.class)
                .setParameter("id", id)
                .setLockMode(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
                .getResultList()
                .stream()
                .findFirst();
    }
}
