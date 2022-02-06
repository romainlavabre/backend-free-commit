# HISTORY

This library permit to keep trace of altered object.

Usage:

```java
import com.replace.replace.api.history.HistoryHandler;
import com.replace.replace.api.request.Request;

public class CreateSample{

    protected final HistoryHandler historyHandler;

    public CreateSample(HistoryHandler historyHandler){
        this.historyHandler = historyHandler;
    }

    public void create(Request request, Sample sample){
    
        ...

        this.historyHandler.create( sample );
    }
}
```

This sample adds trace of Sample creation.


```java
import com.replace.replace.api.history.HistoryHandler;
import com.replace.replace.api.request.Request;

public class UpdateName{

    protected final HistoryHandler historyHandler;

    public UpdateName(HistoryHandler historyHandler){
        this.historyHandler = historyHandler;
    }

    public void update(Request request, Sample sample){
    
        ...

        this.historyHandler.update( sample, SampleProperty.NAME );
    }
}
```

This sample adds trace of Sample update.



```java
import com.replace.replace.api.history.HistoryHandler;
import com.replace.replace.api.request.Request;

public class DeleteSample{

    protected final HistoryHandler historyHandler;

    public DeleteSample(HistoryHandler historyHandler){
        this.historyHandler = historyHandler;
    }

    public void delete(Request request, Sample sample){
    
        ...

        this.historyHandler.delete( sample );
    }
}
```

This sample adds trace of Sample deletion.



### Core

This library take the pertinent values of object for keep good trace.

### Requirements

- Module git@github.com:romainlavabre/spring-starter-event.git

### Versions

##### 1.0.0

INITIAL