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
        taskManager.getAllTasks().forEach((key, value) -> pw.println(value));
        taskManager.getAllEpics().forEach((key, value) -> pw.println(value));
        taskManager.getAllSubTasks().forEach((key, value) -> pw.println(value));

        pw.println();
        pw.println("- Cоздаём две задачи, эпик с тремя подзадачами и эпик без подзадач -");

        Task task1 = new Task("Задача №1", "Проверить уровень масла", TaskStatus.NEW);
        taskManager.addTask(task1);
        Task task2 = new Task("Задача №2", "Проверить давление в шинах", TaskStatus.NEW);
        taskManager.addTask(task2);

        Epic epic1 = new Epic("Эпик №1", "Завести автомобиль");
        taskManager.addEpic(epic1);
        Epic epic2 = new Epic("Эпик №2", "Пустой эпик");
        taskManager.addEpic(epic2);

        SubTask subTask1 = new SubTask("Подзадача №1", "Вставить ключ в замок зажигания", TaskStatus.NEW, 3);
        taskManager.addSubTask(subTask1);
        SubTask subTask2 = new SubTask("Подзадача №2", "Установить коробку передач на нейтраль", TaskStatus.NEW, 3);
        taskManager.addSubTask(subTask2);
        SubTask subTask3 = new SubTask("Подзадача №3", "Повернуть ключ в замке зажигания", TaskStatus.NEW, 3);
        taskManager.addSubTask(subTask3);

        pw.println();
        pw.println("- Задачи/подзадачи/эпики сохранены -");

        pw.println();
        pw.println("- Получение списка всех задач -");
        taskManager.getAllTasks().forEach((key, value) -> pw.println(value));
        taskManager.getAllEpics().forEach((key, value) -> pw.println(value));
        taskManager.getAllSubTasks().forEach((key, value) -> pw.println(value));


        System.out.println();
        System.out.println("- Обновление задачи/подзадачи/эпика -");
        task2 = new Task(task2.getName(), task2.getDescription(), TaskStatus.DONE);
        taskManager.updateTask(task2);
        subTask1 = new SubTask(subTask1.getName(), subTask1.getDescription(), TaskStatus.DONE, 3);
        taskManager.updateSubTask(subTask1);
        subTask2 = new SubTask(subTask2.getName(), subTask2.getDescription(), TaskStatus.DONE, 3);
        taskManager.updateSubTask(subTask2);
        subTask3 = new SubTask(subTask3.getName(), subTask3.getDescription(), TaskStatus.IN_PROGRESS, 3);
        taskManager.updateSubTask(subTask3);


        pw.println();
        pw.println("- Получение списка всех задач -");
        taskManager.getAllTasks().forEach((key, value) -> pw.println(value));
        taskManager.getAllEpics().forEach((key, value) -> pw.println(value));
        taskManager.getAllSubTasks().forEach((key, value) -> pw.println(value));

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
