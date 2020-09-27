/*
 * Copyright 2020 Shubham Panchal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package matrix

import com.ml.projects.frozenlake.matrix.Matrix
import java.lang.RuntimeException
import java.util.*
import kotlin.math.exp

class MatrixOps {

    companion object {

        private fun dot( x1 : DoubleArray , x2 : DoubleArray ) : Double  {
            return DoubleArray( x1.size ).mapIndexed{ index, d -> x1[ index ] * x2[ index ] }.sum()
        }

        fun zerosLike(m : Int, n : Int ) : Matrix {
            return Matrix(m, n)
        }

        fun onesLike(m : Int, n : Int ) : Matrix {
            val out = Matrix(m, n)
            for ( i in 0 until m ) {
                for ( j in 0 until n ) {
                    out.set( i , j , 1.0 )
                }
            }
            return out
        }

        fun exp( mat : Matrix) : Matrix {
            for ( i in 0 until mat.m ) {
                for ( j in 0 until mat.n ) {
                    mat.set( i , j , exp( mat.get( i , j ) ) )
                }
            }
            return mat
        }

        fun sum_along_axis0( mat : Matrix) : Double {
            val _sum = DoubleArray( mat.m )
            for ( i in 0 until mat.m ){
                _sum[ i ] = mat.getData()[ i ].sum()
            }
            return _sum.sum()
        }

        fun max_along_axis0( mat : Matrix) : Double {
            val _max = DoubleArray( mat.m )
            for ( i in 0 until mat.m ){
                _max[ i ] = max_(mat.getData()[i])
            }
            return _max.max()!!
        }

        fun max_along_axis1( mat : Matrix) : Matrix {
            val _max = DoubleArray( mat.m )
            for ( i in 0 until mat.m ){
                _max[ i ] = max_(mat.getData()[i])
            }
            return Matrix(1, mat.m).apply { setData( arrayOf( _max ) ) }
        }

        fun uniform( m : Int , n : Int ) : Matrix {
            val out = Matrix(m, n)
            for ( i in 0 until m ) {
                for ( j in 0 until n ) {
                    out.set( i , j , Random().nextDouble() )
                }
            }
            return out
        }

        fun constant( m : Int , n : Int , c : Double ) : Matrix {
            val out = Matrix(m, n)
            for ( i in 0 until m ) {
                for ( j in 0 until n ) {
                    out.set( i , j , c )
                }
            }
            return out
        }

        fun rand(start: Int, end: Int): Double {
            return (Math.random() * (end - start + 1)) + start
        }

        fun ln( x : Matrix) : Matrix {
            val out = Matrix(x.m, x.n)
            for ( i in 0 until x.m ) {
                for ( j in 0 until x.n ) {
                    out.set( i , j , kotlin.math.ln( x.get( i , j ) ) )
                }
            }
            return out
        }

        fun dot(mat1 : Matrix, mat2 : Matrix) : Matrix {
            if ( mat1.n != mat2.m ) {
                throw RuntimeException( "Shapes incompatible $mat1 and $mat2")
            }
            val product = Matrix(mat1.m, mat2.n)
            for ( i in 0 until mat1.m ){
                for ( j in 0 until mat2.n ) {
                    product.set( i , j ,
                        dot(mat1.getRow(i), mat2.getColumn(j))
                    )
                }
            }
            return product
        }

        private fun max_( x : DoubleArray ) : Double {
            return x.max()!!
        }

    }



}