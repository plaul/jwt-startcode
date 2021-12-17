package kea.sem3.jwtdemo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import kea.sem3.jwtdemo.security.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class NewMemberResponse {
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    LocalDateTime created;
    List<String> roleNames;
    String username;

    public NewMemberResponse( String username,LocalDateTime created, List<Role> roleList) {
        this.created = created;
        this.roleNames = new ArrayList<>();
        this.roleNames = roleList.stream().map(role->role.toString()).collect(Collectors.toList());
        this.username = username;
    }
}
