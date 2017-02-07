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
package com.example

trait Functor[F[_]] {
  def fmap[A, B](m: F[A])(f: A => B): F[B]
}

trait Monad[F[_]] {
  def bind[A, B](m: F[A])(f: A => F[B]): F[B]
  def pure[A]: F[A]
}

sealed trait FreeM[S[+_], +A] {
  def bind[B](f: A => FreeM[S, B])(implicit s: Functor[S]): FreeM[S, B]
  def fmap[B](f: A => B)(implicit s: Functor[S]): FreeM[S, B] = bind(a => Pure(f(a)))
}

case class Pure[S[+_], +A](a: A) extends FreeM[S, A] {
  override def bind[B](f: (A) => FreeM[S, B])(implicit s: Functor[S]): FreeM[S, B] = f(a)
}

case class Free[S[+_], +A](k: S[FreeM[S, A]]) extends FreeM[S, A] {
  override def bind[B](f: (A) => FreeM[S, B])(implicit s: Functor[S]): FreeM[S, B] = Free(s.fmap(k)(_.bind(f)))
}
