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



#### Engine configuration

    <dependency>
        <groupId>se.oyabun.criters</groupId>
        <artifactId>criters-engine</artifactId>
        <version>${criters-engine.version}</version>
    </dependency>
    


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
