public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        System.out.println("- Получение списка всех задач -");
        taskManager.printAllTasks();

        System.out.println();
        System.out.println("- Ввод задач/подзадач/эпиков -");

        Task task1 = new Task("Задача №1", "Проверить уровень масла");
        Task task2 = new Task("Задача №2", "Проверить давление в шинах");
        Task task3 = new Task("Задача №3", "Проверить состояние стеклоочистителей");
        Task task4 = new Task("Задача №4", "Проверить отсутствие течей жидкостей");
        Epic epic1 = new Epic("Эпик №1", "Завести автомобиль");
        Epic epic2 = new Epic("Эпик №2", "Выехать со двора");
        Epic epic3 = new Epic("Эпик №3", "Тестируем статус пустого эпика");
        SubTask subTask1 = new SubTask("Подзадача №1", "Вставить ключ в замок зажигания", epic1);
        SubTask subTask2 = new SubTask("Подзадача №2", "Установить коробку передач на нейтраль", epic1);
        SubTask subTask3 = new SubTask("Подзадача №3", "Повернуть ключ в замке зажигания", epic1);
        SubTask subTask4 = new SubTask("Подзадача №4", "Пристегнуть ремень безопасности", epic2);
        SubTask subTask5 = new SubTask("Подзадача №5", "Открыть ворота парковки", epic2);
        SubTask subTask6 = new SubTask("Подзадача №6", "Установить коробку передач на первую передачу", epic2);
        SubTask subTask7 = new SubTask("Подзадача №7", "Отпустить сцепление", epic2);
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);
        taskManager.addTask(task4);
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        taskManager.addEpic(epic3);
        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);
        taskManager.addSubTask(subTask3);
        taskManager.addSubTask(subTask4);
        taskManager.addSubTask(subTask5);
        taskManager.addSubTask(subTask6);
        taskManager.addSubTask(subTask7);
        System.out.println("- Задачи/подзадачи/эпики сохранены -");

        System.out.println();
        System.out.println("- Получение списка всех задач -");
        taskManager.printAllTasks();

        System.out.println();
        System.out.println("- Получение задачи/подзадачи/эпика по идентификатору -");
        System.out.println(taskManager.getTask(task2.getId()));
        System.out.println(taskManager.getTask(task4.getId()));
        System.out.println(taskManager.getTask(epic2.getId()));
        System.out.println(taskManager.getTask(subTask1.getId()));
        System.out.println(taskManager.getTask(subTask5.getId()));
        System.out.println(taskManager.getTask(subTask6.getId()));

        System.out.println();
        System.out.println("- Вывод истории просмотра задач -");
        taskManager.getHistory().forEach(System.out::println);

        System.out.println();
        System.out.println("- Обновление задачи/подзадачи/эпика -");
        task2 = new Task(task2.getName(), task2.getDescription());
        taskManager.updateTask(task2, 2, TaskStatus.DONE);
        task3 = new Task(task3.getName(), task3.getDescription());
        taskManager.updateTask(task3, 3, TaskStatus.IN_PROGRESS);
        subTask2 = new SubTask(subTask2.getName(), subTask2.getDescription(), epic1);
        taskManager.updateSubTask(subTask2, 9, TaskStatus.DONE);
        subTask4 = new SubTask(subTask4.getName(), subTask4.getDescription(), epic2);
        taskManager.updateSubTask(subTask4, 11, TaskStatus.DONE);
        subTask5 = new SubTask(subTask5.getName(), subTask5.getDescription(), epic2);
        taskManager.updateSubTask(subTask5, 12, TaskStatus.DONE);
        subTask6 = new SubTask(subTask6.getName(), subTask6.getDescription(), epic2);
        taskManager.updateSubTask(subTask6, 13, TaskStatus.DONE);
        epic2 = new Epic(epic2.getId(), "Эпик №2", "Выехать с парковки"); // В эпике статус вручную не меняется.
        taskManager.updateEpic(epic2);
        taskManager.printAllTasks();

        System.out.println();
        System.out.println("- Получение списка всех подзадач определённого эпика -");
        System.out.println(taskManager.getSubTasksInEpic(epic2));

        System.out.println();
        System.out.println("- Удаляем подзадачу №7 со статусом NEW в эпике №2. Проверяем, что эпик перешёл в статус DONE -");
        taskManager.deleteSubTask(subTask7.getId());
        System.out.println(taskManager.getTask(epic2.getId()));

        System.out.println();
        System.out.println("- Удаление задачи/подзадачи/эпика по идентификатору. Вывод списка оставшихся задач -");
        taskManager.deleteTask(task2.getId());
        taskManager.deleteTask(task3.getId());
        taskManager.deleteSubTask(subTask1.getId());
        taskManager.deleteSubTask(subTask5.getId());
        taskManager.deleteEpic(epic2.getId());
        taskManager.printAllTasks();

        System.out.println();
        System.out.println("- Удаление списка задач из трекера -");
        taskManager.deleteAllTasks();

        System.out.println();
        System.out.println("- Получение списка всех задач -");
        taskManager.printAllTasks();

    }
}
