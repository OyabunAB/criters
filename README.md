    ---------------------------------------------------------------------------------------------------
         .aMMMb  dMMMMb  dMP dMMMMMMP dMMMMMP dMMMMb  .dMMMb
        dMP"VMP dMP.dMP amr    dMP   dMP     dMP.dMP dMP" VP
       dMP     dMMMM.  dMP    dMP   dMMMP   dMMMMK   VMMMb
      dMP.aMP dMP AMF dMP    dMP   dMP     dMP AMF dP .dMP
      VMMMP" dMP dMP dMP    dMP   dMMMMMP dMP dMP  VMMMP"
    --------------------------------------------------------------------------------------------------- 
---
# Criters Criteria Automation Engine

[![Build](https://github.com/OyabunAB/criters/actions/workflows/build.yml/badge.svg?branch=master)](https://github.com/OyabunAB/criters/actions/workflows/build.yml)
[![GitHub Packages](https://img.shields.io/badge/GitHub%20Packages-1.0.2-blue)](https://github.com/OyabunAB/criters/packages)

### Configuration
#### Search filter configuration

```kotlin
implementation("se.oyabun.criters:criters-annotation:1.0.2")
```

Annotate your filter objects with the criters filter annotations.
```java
    //
    // Make your object extend Filter<?> for the type of entity you want to find. 
    //
    public class FooFilter extends Filter<Foo> {
        //
        // Use direct parameter restrictions on the entity
        //
        @Parameter(name ="value",
                   restriction = Restriction.EQUALS)
        public Integer getValue() { return 0; }
        //
        // or via a relational property restriction
        //
        @Relations({
            @Relation(name="bars",
                      iterable = true,
                      parameters = {
                @Parameter(name = "id",
                           restriction = Restriction.EQUALS)
            })
        })
        public long getBarId() { return 0L; }
    }
```
#### Engine configuration

```kotlin
implementation("se.oyabun.criters:criters-engine:1.0.2")
```

Use the convenient _Criters_ factory builder to instantiate your criters factory.
You then need to configure the factory to use an entity manager or a root, criteria query and criteria builder.     
```java  
    //
    // Either configure an entity manager
    //
    Criters.<Foo, Filter<Foo>> factory().use(entityManager);
    //
    // or with the root, criteria query and criteria builder directly
    //
    Criters.<Foo, Filter<Foo>> factory().use(root, criteriaQuery, criteriaBuilder);
``` 
#### Spring Data JPA

```kotlin
implementation("se.oyabun.criters:criters-spring-data-jpa:1.0.2")
```

Annotate your filter object the regular way and let your specifications extend _CritersSpecification<E, F extends Filter\<E\>>_.
```java
   public class FooSpecification
           extends CritersSpecification<Foo, Filter<Foo>> {
   
       public FooSpecification(final Filter<Foo> searchFilter) {
   
           super(searchFilter);
   
       }
   
   }
```
### Installation

#### Gradle (Kotlin DSL)

```kotlin
implementation("se.oyabun.criters:criters-engine:1.0.2")
```

#### Gradle (Groovy DSL)

```groovy
implementation 'se.oyabun.criters:criters-engine:1.0.2'
```

### Project Structure

    ╔════════════════════╗ 
    ║ Criters Project    ║
    ╚═╤══════════════════╝ 
      │   ╔═════════════════════╗
      ├───╢ Criters Annotations ║
      │   ╚═════════════════════╝
      │   ╔════════════════╗
      ├───╢ Criters Engine ║
      │   ╚════════════════╝
      │   ╔═════════════════════════╗
      ├───╢ Criters Spring Data JPA ║
      │   ╚═════════════════════════╝
      │   ╔════════════════════════╗
      ├───╢ Criters Hibernate Test ║
      │   ╚════════════════════════╝
      │   ╔══════════════════════════╗
      ├───╢ Criters EclipseLink Test ║
      │   ╚══════════════════════════╝
      │   ╔═══════════════════════╗
      ├───╢ Criters Core JPA Test ║
      │   ╚═══════════════════════╝
      │   ╔═══════════════════╗
      └───╢ Criters Core Test ║
          ╚═══════════════════╝

### Publishing

The project is published to:
- **[GitHub Packages](https://github.com/OyabunAB/criters/packages)** — Released on new version tags (`v*`)

### Building

```bash
./gradlew build          # Build all modules
./gradlew build -x test  # Build without running tests
./gradlew clean          # Clean build artifacts
```

Requires Java 21 and Gradle 9.4.1+