package ru.yandex.practicum.task_tracker.history;

import ru.yandex.practicum.task_tracker.tasks.Task;

import java.util.*;

public class OptimizedSprintFiveLinkedList<T> {
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
        if (nodeTaskHistory.containsKey(id)) {
            removeNode(nodeTaskHistory.get(id));
            nodeTaskHistory.remove(id);
        }
    }

    public void removeNode(Node<Task> node) {
        if ((nodeTaskHistory.containsKey(node.getData().getId()))) {
            Node<Task> prevNode = node.getPrev();
            Node<Task> nextNode = node.getNext();
            if ((prevNode == null) && (nextNode != null)) {
                nextNode.setPrev(null);
                head = nextNode;
            } else if ((nextNode == null) && (prevNode != null)) {
                prevNode.setNext(null);
                tail = prevNode;
            } else {
                assert prevNode != null;
                prevNode.setNext(nextNode);
                nextNode.setPrev(prevNode);
            }
            nodeTaskHistory.remove(node.getData().getId());
        }
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }
}