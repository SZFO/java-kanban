import managers.Managers;
import managers.memory.TaskManager;
import tasks.*;

import java.io.PrintWriter;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        //  Создаём объект типа PrintWriter для консольного вывода с автоматической очисткой потока вывода

        PrintWriter pw = new PrintWriter(System.out, true);

        pw.println("- Получение списка всех задач -");
        taskManager.getAllTasks().forEach(pw::println);
        taskManager.getAllEpics().forEach(pw::println);
        taskManager.getAllSubTasks().forEach(pw::println);

        pw.println();
        pw.println("- Cоздаём две задачи, эпик с тремя подзадачами и эпик без подзадач -");

        Task task1 = new Task("Задача №1", "Проверить уровень масла", TaskStatus.NEW);
        Task task2 = new Task("Задача №2", "Проверить давление в шинах", TaskStatus.NEW);
        Epic epic1 = new Epic("Эпик №1", "Завести автомобиль");
        Epic epic2 = new Epic("Эпик №2", "Пустой эпик");
        SubTask subTask1 = new SubTask("Подзадача №1", "Вставить ключ в замок зажигания", TaskStatus.NEW, epic1);
        SubTask subTask2 = new SubTask("Подзадача №2", "Установить коробку передач на нейтраль", TaskStatus.NEW, epic1);
        SubTask subTask3 = new SubTask("Подзадача №3", "Повернуть ключ в замке зажигания", TaskStatus.NEW, epic1);
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);
        taskManager.addSubTask(subTask3);
        pw.println();
        pw.println("- Задачи/подзадачи/эпики сохранены -");

        pw.println();
        pw.println("- Получение списка всех задач -");
        taskManager.getAllTasks().forEach(pw::println);
        taskManager.getAllEpics().forEach(pw::println);
        taskManager.getAllSubTasks().forEach(pw::println);


        System.out.println();
        System.out.println("- Обновление задачи/подзадачи/эпика -");
        task2 = new Task(task2.getName(), task2.getDescription(), TaskStatus.DONE);
        taskManager.updateTask(task2);
        subTask1 = new SubTask(subTask1.getName(), subTask1.getDescription(), TaskStatus.DONE, epic1);
        taskManager.updateSubTask(subTask1);
        subTask2 = new SubTask(subTask2.getName(), subTask2.getDescription(), TaskStatus.DONE, epic1);
        taskManager.updateSubTask(subTask2);
        subTask3 = new SubTask(subTask3.getName(), subTask3.getDescription(), TaskStatus.DONE, epic1);
        taskManager.updateSubTask(subTask3);


        pw.println();
        pw.println("- Получение списка всех задач -");
        taskManager.getAllTasks().forEach(pw::println);
        taskManager.getAllEpics().forEach(pw::println);
        taskManager.getAllSubTasks().forEach(pw::println);

        pw.println();
        pw.println("- Получение задачи/подзадачи/эпика по идентификатору -");
        pw.println(taskManager.getTask(task2.getId()));
        pw.println(taskManager.getEpic(epic2.getId()));
        pw.println(taskManager.getSubTask(subTask1.getId()));

        pw.println();
        pw.println("- Вывод истории просмотра задач -");
        taskManager.getHistory().forEach(pw::println);

        pw.println();
        pw.println("- Получение задачи/подзадачи/эпика по идентификатору -");
        pw.println(taskManager.getTask(task1.getId()));
        pw.println(taskManager.getEpic(epic1.getId()));
        pw.println(taskManager.getSubTask(subTask1.getId()));
        pw.println(taskManager.getSubTask(subTask2.getId()));

        pw.println();
        pw.println("- Вывод истории просмотра задач -");
        taskManager.getHistory().forEach(pw::println);

        pw.println();
        pw.println("- Получение задачи/подзадачи/эпика по идентификатору -");
        pw.println(taskManager.getTask(task2.getId()));
        pw.println(taskManager.getEpic(epic2.getId()));
        pw.println(taskManager.getSubTask(subTask3.getId()));
        pw.println(taskManager.getSubTask(subTask1.getId()));

        pw.println();
        pw.println("- Вывод истории просмотра задач -");
        taskManager.getHistory().forEach(pw::println);

        pw.println();
        pw.println("- Удаление задачи №2 по идентификатору -");
        taskManager.deleteTask(task2.getId());

        pw.println();
        pw.println("- Вывод истории просмотра задач -");
        taskManager.getHistory().forEach(pw::println);

        pw.println();
        pw.println("- Удаление эпика №1 с тремя подзадачами по идентификатору -");
        taskManager.deleteEpic(epic1.getId());

        pw.println();
        pw.println("- Вывод истории просмотра задач -");
        taskManager.getHistory().forEach(pw::println);
    }
}
