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
[![Maven Central](https://img.shields.io/maven-central/v/se.oyabun.criters/criters-engine?label=Maven%20Central)](https://central.sonatype.com/artifact/se.oyabun.criters/criters-engine)
[![GitHub Packages](https://img.shields.io/badge/GitHub%20Packages-Latest-blue)](https://github.com/OyabunAB/criters/packages)

### Configuration
#### Search filter configuration

    <dependency>
        <groupId>se.oyabun.criters</groupId>
        <artifactId>criters-annotation</artifactId>
        <version>${criters-annotation.version}</version>
    </dependency>

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

    <dependency>
        <groupId>se.oyabun.criters</groupId>
        <artifactId>criters-engine</artifactId>
        <version>${criters-engine.version}</version>
    </dependency>
    
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

    <dependency>
        <groupId>se.oyabun.criters</groupId>
        <artifactId>criters-spring-data-jpa</artifactId>
        <version>${criters-engine.version}</version>
    </dependency>

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

#### Maven

```xml
<dependency>
    <groupId>se.oyabun.criters</groupId>
    <artifactId>criters-engine</artifactId>
    <version>${criters-engine.version}</version>
</dependency>
```

#### Gradle

```gradle
implementation 'se.oyabun.criters:criters-engine:${criters-engine.version}'
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
- **[Maven Central](https://central.sonatype.com/artifact/se.oyabun.criters/criters-engine)** — Release versions
- **[GitHub Packages](https://github.com/OyabunAB/criters/packages)** — All builds (snapshots and releases)

### Building

```bash
./gradlew build          # Build all modules
./gradlew build -x test  # Build without running tests
./gradlew clean          # Clean build artifacts
```

Requires Java 21 and Gradle 9.4.1+