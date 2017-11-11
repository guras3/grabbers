package com.example.twittergrabber.repository

import com.example.twittergrabber.domain.Post
import org.springframework.data.mongodb.repository.MongoRepository

interface Repository : MongoRepository<Post, String>