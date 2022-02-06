# Event

### Create event

Configuration class:

```java
com.replace.replace.configuration.event.EventConfiguration
```

Create your function:

```java
import com.replace.replace.configuration.event.Event;
import com.replace.replace.api.event.annotation.UnitEvent;
import org.springframework.stereotype.Service;
import java.util.HashMap;

@Service
public class EventConfiguration implements Event{


    // Event name created in Event interface with constant
    @UnitEvent(name = EVENT_NAME) 
    public Map<String, Class> eventName(){
        Map<String, Class> credentials = new HashMap();

        credentials.put("First param", String.class);
        credentials.put("Second param", Integer.class);

        return credentials;
    }
    
}
```

Your credentials must contains the keys searched (First Param, Second Param ...) 
and the classname.

The EventDispatcher will see if is valid credentials for launched event and throw a

```java
com.replace.replace.api.event.exception.InvalidEventCredentials
``` 

if parameter contains invalid value.

WARNING: The NULL value is considered as valid.

Your event is now created.

### Configure listeners

You have two way:

#### Service with application scope

```java
package com.replace.replace.tutorial;

import com.replace.replace.api.event.EventSubscriber;
import com.replace.replace.api.event.EventDispatcher;
import com.replace.replace.configuration.event.Event;

@Service
public class EventTutorial implements EventSubscriber{

    public EventTutorial(EventDispatcher eventDispatcher){
        eventDispatcher.follow(Event.EVENT_NAME, this);
    }

    public void receiveEvent(String event, Map<String, Object> params ) {

        String firstParam = params.get("First Param");
        Integer secondParam = params.get("Second Param");

        // TODO do something ...
    }

}

```

This configuration work only for service with scope application.
You should never use the EventDispatcher.follow method for another scope.

For it, see the next sample

#### Service with other scope

```java
package com.replace.replace.tutorial;

import com.replace.replace.api.event.EventSubscriber;

@Service
@RequestScope
public class EventTutorial implements EventSubscriber{

    public void receiveEvent(String event, Map<String, Object> params ) {

        String firstParam = params.get("First Param");
        Integer secondParam = params.get("Second Param");

        // TODO do something ...
    }

}

```

Now, pass the proxy directly to the EventConfiguration


```java
import com.replace.replace.configuration.event.Event;
import com.replace.replace.api.event.EventSubscriber;
import com.replace.replace.api.event.annotation.Subscribers;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;


@Service
public class EventConfiguration implements Event{

    private EventTutorial eventTutorial;

    public EventConfiguration(EventTutorial eventTutorial){
        this.eventTutorial = eventTutorial;
    }


    @Subscribers(event = EVENT_NAME)
    public List<EventSubscriber> subscribersEventName(){
        List<EventSubscriber> subscribers = new ArrayList<>();
        
        subscribers.add( this.eventTutorial );

        return subscribers;
    }
    
}
```

Your service is now registered as listener for EVENT_NAME.


### Requirements

Nothing

### Versions

##### 1.0.0

INITIAL