import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private CustomLinkedList<Task> taskViewsHistory;

    public InMemoryHistoryManager() {
        this.taskViewsHistory = new CustomLinkedList<>();
    }

    @Override
    public void add(Task task) {
        taskViewsHistory.linkLast(task);
    }

    @Override
    public List<Task> getHistory() {
        return taskViewsHistory.getTasks();
    }

    @Override
    public void remove(int id) {
        taskViewsHistory.remove(id);
    }

    public static class CustomLinkedList<T> {
        private Map<Integer, Node<Task>> nodeTaskHistory = new HashMap<>();
        private Node<Task> head;
        private Node<Task> tail;
        private int size = 0;

        public void linkLast(Task element) {
            final Node<Task> oldTail = tail;
            final Node<Task> newNode = new Node<>(oldTail, element, null);
            tail = newNode;

            if (oldTail == null) {
                head = newNode;
            } else {
                oldTail.setNext(newNode);
            }
            if (nodeTaskHistory.containsKey(element.getId())) {
                removeNode(nodeTaskHistory.get(element.getId()));
            }
            nodeTaskHistory.put(element.getId(), tail);
            size++;

        }

        public List<Task> getTasks() {
            List<Task> tasksHistory = new ArrayList<>();
            if (head != null) {
                Node<Task> node = head;
                while (node != null) {
                    tasksHistory.add(node.getData());
                    node = node.getNext();
                }
            }
            Collections.reverse(tasksHistory);
            return tasksHistory;
        }

        public void remove(int id) {
            removeNode(nodeTaskHistory.get(id));
        }

        public void removeNode(Node<Task> node) {
            if (node != null) {
                if ((nodeTaskHistory.containsKey(node.getData().getId()))) {
                    Node<Task> prevNode = node.getPrev();
                    Node<Task> nextNode = node.getNext();
                    if (prevNode != null) {
                        prevNode.setNext(nextNode);
                    }
                    if (nextNode != null) {
                        nextNode.setPrev(prevNode);
                    }
                    nodeTaskHistory.remove(node.getData().getId());
                    if (head == node) {
                        head = nextNode;
                    } else if (tail == node) {
                        tail = nextNode;
                    }
                }
            }
        }

        public int size() {
            return size;
        }

        public boolean isEmpty() {
            return size == 0;
        }

    }
}
