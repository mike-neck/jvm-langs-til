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

sealed trait Operation[+A]

case class GetString[+A](f: String => A) extends Operation[A]
case class PutString[+A](s: String, x: A) extends Operation[A]

object Operation {
  implicit val operationFunctor = new Functor[Operation] {
    override def fmap[A, B](m: Operation[A])(f: (A) => B): Operation[B] = m match {
      case GetString(s)    => GetString(f compose s)
      case PutString(s, x) => PutString(s, f(x))
    }
  }

  def getString: FreeM[Operation, String] = Free(GetString({s => Pure(s)}))
  def putString(s: String): FreeM[Operation, Unit] = Free(PutString(s, Pure()))

  def mapM_[S[+_]: Functor, A](f: A => FreeM[S, Unit], seq: Seq[A]): FreeM[S, Unit] = seq.toList match {
    case x::xs => xs.foldLeft(f(x)){ (m: FreeM[S, Unit], s: A) => m.bind { _ => f(s) } }
    case Nil   => Pure(())
  }
}
