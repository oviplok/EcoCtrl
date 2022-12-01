package it.mirea.ecoctrl.repositories.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.google.gson.reflect.TypeToken;

import java.util.Map;

import it.mirea.ecoctrl.di.ServiceLocator;
import it.mirea.ecoctrl.domain.models.User;

@Entity(tableName = "user", primaryKeys = {"id"}, ignoredColumns = {"connections"})
public class UserDTO extends User {
    @ColumnInfo
    private String connectionsDTO;

    public String getConnectionsDTO() {
        return connectionsDTO;
    }

    public void setConnectionsDTO(String connectionsDTO) {
        this.connectionsDTO = connectionsDTO;
        super.setConnections(ServiceLocator.getInstance().getGson().fromJson(connectionsDTO, new TypeToken<Map<String, String>>() {}.getType()));
    }

    @Override
    public void setConnections(Map<String, String> connections) {
        super.setConnections(connections);
        this.connectionsDTO = ServiceLocator.getInstance().getGson().toJson(connections);
    }

    @Override
    public Map<String, String> getConnections() {
        if (super.getConnections() == null || super.getConnections().isEmpty()) {
            super.setConnections(ServiceLocator.getInstance().getGson().fromJson(
                    this.connectionsDTO,
                    new TypeToken<Map<String, String>>() {}.getType()
            ));
        }
        return super.getConnections();
    }

    public static UserDTO convertFromUser(User person) {
        UserDTO dto = new UserDTO();

        dto.setId(person.getId());
        dto.setFirst_name(person.getFirst_name());
        dto.setLast_name(person.getLast_name());
        dto.setEmail(person.getEmail());
        dto.setPassword(person.getPassword());
        dto.setPhone(person.getPhone());
        dto.setRole(person.getRole());
        dto.setConnections(person.getConnections());

        return dto;
    }

}
