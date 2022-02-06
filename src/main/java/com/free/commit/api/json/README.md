# Encoder Json

The json encoder permit to manage the output for object,
you can apply easier the filters, or ignored property etc.

### Configure your object

#### Default
By default, your configuration is

```java
import com.replace.replace.api.json.annotation.Group;
import com.replace.replace.api.json.annotation.Json;

public class Sample{

    @Json(groups = {
        @Group
    })
    private int id;

    @Json(groups = {
        @Group
    })
    private String otherField1;

    private String otherField2;
}
```

For this case, it will produce this output:

```json
{
  "id": integer,
  "other_field_1": "String"
}
```

NOTE: by default, the fields are formatted as a snack case

#### Rename key


```java
import com.replace.replace.api.json.annotation.Group;
import com.replace.replace.api.json.annotation.Json;

public class Sample{

    @Json(groups = {
        @Group(key = "custom_id")
    })
    private int id;
}
```

output:
```json
{
  "custom_id": integer
}
```

#### Apply Overwrite

For know how build a custom overwrite, see the section "Build Overwrite"
 
```java
import com.replace.replace.api.json.annotation.Group;
import com.replace.replace.api.json.annotation.Json;

public class Sample{

    @Json(groups = {
        @Group(overwrite = RenameToClientName.class)
    })
    private String name;
}
```

output:
```json
{
  "name": "company name replaced by client name"
}
```

#### Specify relation


By default, encoder only encodes sub class when it contains the JPA @Entity annotation.
If you want to force this encoding, specify this :
 
```java
import com.replace.replace.api.json.annotation.Group;
import com.replace.replace.api.json.annotation.Json;

public class Sample{

    @Json(groups = {
        @Group(forceEncoding = true)
    })
    private Sample1 sample1;
}
```

output:
```json
{
  "sample1_id": Id of ressource
}
```

NOTE: When subclass is encoded, the suffix "_id" is automatically added   

#### Encode object

 
```java
import com.replace.replace.api.json.annotation.Group;
import com.replace.replace.api.json.annotation.Json;

public class Sample{

    @Json(groups = {
        @Group(onlyId = false)
    })
    private Sample1 sample1;
}
```

output:
```json
{
  "sample1": {
     "properties": values
  }
}
```

#### Merge ascent object


```java
import com.replace.replace.api.json.annotation.Group;
import com.replace.replace.api.json.annotation.Json;

public class Sample{

    @Json(groups = {
        @Group
    })
    private String name;

    @Json(groups = {
        @Group(onlyId = false, ascent = true)
    })
    private Sample1 sample1;
}
```

output:
```json
{
  "name": "name",
  "properties of sample 1": values
}
```


#### Multiple configurations

```java
import com.replace.replace.configuration.json.GroupType;
import com.replace.replace.api.json.annotation.Group;
import com.replace.replace.api.json.annotation.Json;

public class Sample{

    @Json(groups = {
        @Group,
        @Group(name = GroupType.GROUP_NAME)
    })
    private String name;

    @Json(groups = {
        @Group( onlyId = false, ascent = true),
        @Group(name = GroupType.GROUP_NAME, object = true, key = "sample1_id")
    })
    private Sample1 sample1;
}
```

#### Add row

```java
import com.replace.replace.configuration.json.GroupType;
import com.replace.replace.api.json.annotation.Group;
import com.replace.replace.api.json.annotation.JsonPut;
import com.replace.replace.api.json.annotation.Row;
import com.replace.replace.api.json.put.Put;


@JsonPut( group = {
        @Group( name = GroupType.GROUP_NAME, row = {
                @Row( key = "key", handler = Put.class )
        } )
} )

public class Sample{

    ...
}
```

### Encode your data

```java
import com.replace.replace.api.json.Encoder;import com.replace.replace.configuration.json.GroupType;import java.util.Map;

public class SampleController {

    public Map<String, Object> getSample(Sample sample){
        
        return Encoder.encode( sample, GroupType.DEFAULT );
    }
}
```


### Requirements

Nothing

### Versions

##### 2.0.0

- ADD Optimize performance
- CHANGE automatically encodes the sub class if it contains @Entity annotation
- ADD Default field name format is in snack case
- CHANGE Allow injection of the Overwrite & Put class by the dependency injection container


##### 1.0.0

INITIAL