import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private List<Integer> subTasks;

    public Epic(String name, String description) {
        super(name, description);
        this.subTasks = new ArrayList<>();
    }

   public Epic(Integer id, String name, String description ) {
        super(id, name, description);
    }

    public List<Integer> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(List<Integer> subTasks) {
        this.subTasks = subTasks;
    }

    public void setSubTaskId(int subTaskId) {
        subTasks.add(subTaskId);
    }


    @Override
    public int hashCode() {
        int hash = 17;
        if (super.getId() != 0) {
            hash = hash + super.getId().hashCode();
        }
        hash = hash * 31;

        if (super.getName() != null) {
            hash = hash + super.getName().hashCode();
        }
        hash = hash * 31;

        if (super.getDescription() != null) {
            hash = hash + super.getDescription().hashCode();
        }
        hash = hash * 31;

        if (super.getStatus() != null) {
            hash = hash + super.getStatus().hashCode();
        }
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Epic epic = (Epic) obj;
        return Objects.equals(super.getId(), epic.getId()) &&
                Objects.equals(super.getName(), epic.getName()) &&
                Objects.equals(super.getDescription(), epic.getDescription()) &&
                Objects.equals(super.getStatus(), epic.getStatus());
    }

    @Override
    public String toString() {
        return "Эпик{" +
                "Название эпика='" + super.getName() + '\'' +
                ", Описание эпика='" + super.getDescription() + '\'' +
                ", Статус эпика=" + super.getStatus() +
                ", ID эпика=" + super.getId() +
                '}';
    }


}
