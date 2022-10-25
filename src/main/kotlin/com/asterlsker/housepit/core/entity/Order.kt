package com.asterlsker.housepit.core.entity

import javax.persistence.*

@Entity
class Order(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long? = null,
    var name: String
)