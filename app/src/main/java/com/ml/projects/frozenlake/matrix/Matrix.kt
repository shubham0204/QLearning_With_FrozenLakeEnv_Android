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

package com.ml.projects.frozenlake.matrix

class Matrix(val m : Int, val n : Int, var name : String = "matrix" ) {

    private var data = Array( m ){ DoubleArray( n ) }

    fun getRow( i : Int ) : DoubleArray {
        return data[ i ]
    }

    fun getData() : Array<DoubleArray> {
        return data
    }

    fun transpose() : Matrix {
        val transpose = Matrix(n, m)
        for ( i in 0 until m ) {
            for ( j in 0 until n ) {
                transpose.set( j , i , data[ i ][ j ] )
            }
        }
        return transpose
    }

    fun setData( data : Array<DoubleArray> ) {
        this.data = data
    }

    fun get( i : Int , j : Int ) : Double {
        return data[ i ][ j ]
    }

    fun set( i : Int , j : Int , element : Double ){
        data[ i ][ j ] = element
    }

    override fun toString(): String {
        return ( "$name Shape( $m * $n ) ${data.contentDeepToString()}" )
    }

    fun getColumn( j : Int ) : DoubleArray {
        assert( j < n )
        val column = DoubleArray( m ).mapIndexed{ index, _ -> data[ index ][ j ] }
        return column.toDoubleArray()
    }

    operator fun plus(mulV: Matrix): Matrix {
        val sum = Matrix(mulV.m, mulV.n)
        for ( i in 0 until mulV.m ) {
            for ( j in 0 until mulV.n ) {
                sum.set( i , j , mulV.get( i , j ) + mulV.get( i , j ) )
            }
        }
        return sum
    }

    operator fun minus(mulV: Matrix): Matrix {
        val difference = Matrix(mulV.m, mulV.n)
        for ( i in 0 until mulV.m ) {
            for ( j in 0 until mulV.n ) {
                difference.set( i , j , data[ i ][ j ] - mulV.get( i , j ) )
            }
        }
        return difference
    }

    operator fun minus( v : Double ): Matrix {
        val difference = Matrix(m, n)
        for ( i in 0 until m ) {
            for ( j in 0 until n ) {
                difference.set( i , j , data[ i ][ j ] - v )
            }
        }
        return difference
    }

    operator fun times( m2 : Matrix?): Matrix {
        val product = Matrix(m2!!.m, m2!!.n)
        for ( i in 0 until m2.m ) {
            for ( j in 0 until m2.n ) {
                product.set( i , j , data[ i ][ j ] * m2.get( i , j ) )
            }
        }
        return product
    }

    operator fun times( c : Double? ): Matrix {
        val product = Matrix(m, n)
        for ( i in 0 until m ) {
            for ( j in 0 until n ) {
                product.set( i , j , data[ i ][ j ] * c!! )
            }
        }
        return product
    }

}