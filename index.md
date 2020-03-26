# Player Storage

This is a library for managing the storage of players with the ability
to add or update, delete, get a rank, and roll back.

## Table Of Contents
<!--ts-->
   * [Overview](#overview)
        * [Operations](#operations)
   * [Using in your project](#using-in-your-project)
        * [Maven](#maven)
        * [Gradle](#gradle)
   * [Performance](#performance)
        * [JMH Benchmark](#jmh-benchmark)
<!--te-->

## Overview
This library contains the thread-safe class PlayerStorage which implements interface Storage.

### Operations
Class PlayerStorage implements the following methods.

#### registerPlayerResult
Adds a player with a rating or updates a player rating, if one has been already added.  

```kotlin
@Synchronized
fun registerPlayerResult(playerName: String, playerRating: Int): Boolean
```
**Note:** This method is marked as @Synchronized.

#### unregisterPlayer
Deletes the player from the storage.

```kotlin
@Synchronized
fun unregisterPlayer(playerName: String): Boolean
```
**Note:** This method is marked as @Synchronized.

#### getPlayerRank
Returns player rank. Rank is a position in the rating table. 
```kotlin
fun getPlayerRank(playerName: String): Int?
```

#### rollback
Rolls the last [step] registerPlayerResult or unregisterPlayer invocations back.
```kotlin
@Synchronized
fun rollback(step: Int): Boolean
```
**Note:** This method is marked as @Synchronized.

## Using in your project
The library published to JitPack repository.
### Maven
Step 1. Add the JitPack repository to your build file.

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

Step 2. Add the dependency.

```xml
<dependency>
    <groupId>com.github.akifev</groupId>
    <artifactId>player-storage</artifactId>
    <version>1.0</version>
</dependency>
```
### Gradle
Step 1. Add the JitPack repository to your build file.

```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Step 2. Add the dependency.

```groovy
dependencies {
        implementation 'com.github.akifev:player-storage:1.0'
}
```

## Performance
### JMH Benchmark 
Here is the relation of average operation execution time to the quantity of players added to the storage.   

|Quantity of players | 14    | 62    | 1022  | 8190  | 65534 |         |
|:-------------------|:-----:|:-----:|:-----:|:-----:|:-----:|--------:|
|registerPlayerResult|307    |510    |1079   |1984   |4156   |ns/op    |
|unregisterPlayer    |313    |585    |1123   |1974   |4385   |ns/op    |
|getPlayerRank       |135    |239    |544    |1104   |2328   |ns/op    |
|rollback            |58     |50     |59     |31     |9      |ns/op    |
