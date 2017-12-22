# PAD-lab2

### Deployment Diagram

![image](https://github.com/Alexx-G/PAD-labs/raw/master/images/l2_schema.png)


### Node's data

```java
public class University implements Serializable, Cloneable {

    private String name;
    private String country;
    private Integer rating;
    private List<String> studentList;
}
```

### Features implemented
- Serialization in different formats
**Node** will send the information to Mediator in **JSON** format
**Mediator** will send the information to Client in **XML**

- Interogation
The client can interogate mediator for filtering the **Student** names:
```xml
Enter filter value for students name, just press ENTER for getting all students:
luca
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<university>
    <country>Moldova</country>
    <name>UTM</name>
    <rating>10</rating>
    <studentList>Luca</studentList>
</university>
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<university>
    <country>Moldova</country>
    <name>USM</name>
    <rating>7</rating>
</university>
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<university>
    <country>Moldova</country>
    <name>ASEM</name>
    <rating>6</rating>
</university>
```


- Format negotiation
Client can receive data in ``XML`` or ``JSON``. Example:

```json
Enter filter value for students name, just press ENTER for getting all students:
viorica?json
{"name":"UTM","country":"Moldova","rating":10,"studentList":[]}{"name":"USM","country":"Moldova","rating":7,"studentList":[]}{"name":"ASEM","country":"Moldova","rating":6,"studentList":[]}
```

Default is xml

```xml
Enter filter value for students name, just press ENTER for getting all students:
victoria?xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<university>
    <country>Moldova</country>
    <name>UTM</name>
    <rating>10</rating>
</university>
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<university>
    <country>Moldova</country>
    <name>USM</name>
    <rating>7</rating>
</university>
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<university>
    <country>Moldova</country>
    <name>ASEM</name>
    <rating>6</rating>
    <studentList>Victoria</studentList>
</university>
```
