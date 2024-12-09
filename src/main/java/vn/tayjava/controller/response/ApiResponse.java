package vn.tayjava.controller.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.Serializable;

@Builder
@Getter
@Setter
public class ApiResponse implements Serializable {
    private int status;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object data;

    private void writeObject(java.io.ObjectOutputStream stream)
            throws IOException {
        stream.defaultWriteObject();
    }

    private void readObject(java.io.ObjectInputStream stream)
            throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
    }
}
