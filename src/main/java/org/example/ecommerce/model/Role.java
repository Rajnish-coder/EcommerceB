package org.example.ecommerce.model;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Entity
@NoArgsConstructor
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "role_id")
    private Long roleId;

    @Column(length = 20,name="role_name")
    @Enumerated(EnumType.STRING)
    @ToString.Exclude
    private AppRole roleName;


    public Role() {
    }

    public Role(AppRole roleName) {
        this.roleName = roleName;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public AppRole getRole() {
        return roleName;
    }

    public void setRole(AppRole roleName) {
        this.roleName = roleName;
    }
}
