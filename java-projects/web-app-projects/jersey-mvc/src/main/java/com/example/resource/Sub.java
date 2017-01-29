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
package com.example.resource;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Path("/sub")
public class Sub {

    @Data
    @RequiredArgsConstructor
    public static class Help {
        private final String message;
        private final List<String> nextResource;
    }

    @GET
    public Help help() {
        return new Help(
                "calculation of two numbers"
                , Arrays.stream(Calc.values()).map(Calc::name).map(String::toLowerCase).collect(toList())
        );
    }

    @Path("{calc}")
    public Calculation calculation(@PathParam("calc") String calc) {
        return new Calculation(Calc.fromString(calc));
    }

    public static class Calculation {

        private final Calc calc;

        private Calculation(Calc calc) {
            this.calc = calc;
        }

        @GET
        @Produces({MediaType.APPLICATION_JSON})
        @Path("{left: \\-?[0-9]{1,7}}/{right: \\-?[0-9]{1,7}}")
        public CalculationResult runCalc(@PathParam("left") int left, @PathParam("right") int right) {
            return calc.runCalc(left, right);
        }
    }

    private enum Calc {
        ADD {
            @Override
            CalculationResult runCalc(int left, int right) {
                final int result = left + right;
                return new CalculationResult(left, right, result, String.format("%d + %d = %d", left, right, result));
            }
        }, SUB {
            @Override
            CalculationResult runCalc(int left, int right) {
                final int result = left - right;
                return new CalculationResult(left, right, result, String.format("%d - %d = %d", left, right, result));
            }
        }, MULTIPLY {
            @Override
            CalculationResult runCalc(int left, int right) {
                final int result = left * right;
                return new CalculationResult(left, right, result, String.format("%d x %d = %d", left, right, result));
            }
        }, DIVIDE {
            @Override
            CalculationResult runCalc(int left, int right) {
                if (right == 0) throw new IllegalArgumentException("div by 0.");
                final int result = left / right;
                return new CalculationResult(left, right, result, String.format("%d / %d = %d", left, right, result));
            }
        }, CONCAT {
            @Override
            CalculationResult runCalc(int left, int right) {
                final String result = String.format("%d%d", left, right);
                return new CalculationResult(
                        left
                        , right
                        , result
                        , String.format("\"%d\" + \"%d\" -> \"%s\"", left, right, result));
            }
        };

        abstract CalculationResult runCalc(int left, int right);

        @NotNull
        @Contract("null -> fail")
        public static Calc fromString(String calc) {
            if (calc == null) throw new NotFoundException("null resource not found.");
            try {
                return valueOf(calc.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new NotFoundException(String.format("%s is not invalid resource name.", calc));
            }
        }
    }

    @Data
    @RequiredArgsConstructor
    @XmlRootElement
    public static class CalculationResult {
        private final int left;
        private final int right;
        private final String result;
        private final String calculation;

        public CalculationResult(int left, int right, int result, String calculation) {
            this(left, right, Integer.toString(result), calculation);
        }
    }
}
