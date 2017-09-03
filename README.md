    ---------------------------------------------------------------------------------------------------
         .aMMMb  dMMMMb  dMP dMMMMMMP dMMMMMP dMMMMb  .dMMMb
        dMP"VMP dMP.dMP amr    dMP   dMP     dMP.dMP dMP" VP
       dMP     dMMMM.  dMP    dMP   dMMMP   dMMMMK   VMMMb
      dMP.aMP dMP AMF dMP    dMP   dMP     dMP AMF dP .dMP
      VMMMP" dMP dMP dMP    dMP   dMMMMMP dMP dMP  VMMMP"
    --------------------------------------------------------------------------------------------------- 
---
# Criters Criteria Automation Engine

### Configuration

#### Search criteria configuration

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
        @ParameterFilter(restriction = Restriction.EQUALS,
                         sourceParameterName ="value")
        public Integer getValue() { return 0; }
      
        //
        // or via a relational property restriction
        //
        @RelationFilter(restriction = Restriction.IN,
                        sourceParameterName = "bars",
                        relationSourceCollection = true,
                        relationTargetType = Bar.class,
                        relationTargetParameter = "id")
        public long getBarId() { return 0L; }
    }
```

#### Engine configuration

    <dependency>
        <groupId>se.oyabun.criters</groupId>
        <artifactId>criters-engine</artifactId>
        <version>${criters-engine.version}</version>
    </dependency>
    
Use the conveniet _Criters_ factory builder to instantiate your criters factory.
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

Annotate your filter object the regular way and let your specifications extend _CritersSpecification<?>.
```java
   public class FooSpecification
           extends CritersSpecification<Foo, Filter<Foo>> {
   
       public FooSpecification(final Filter<Foo> searchFilter) {
   
           super(searchFilter);
   
       }
   
   }
```
### Project Structure

    ╔═════════════════════════╗ 
    ║ Criters Project Reactor ║
    ╚═╤═══════════════════════╝ 
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
