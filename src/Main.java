public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        System.out.println("- Получение списка всех задач -");
        taskManager.printListAllTasks();

        System.out.println();
        System.out.println("- Ввод задач/подзадач/эпиков -");

        Task task1 = new Task("Задача №1", "Проверить уровень масла");
        Task task2 = new Task("Задача №2", "Проверить давление в шинах");
        Task task3 = new Task("Задача №3", "Проверить состояние стеклоочистителей");
        Task task4 = new Task("Задача №4", "Проверить отсутствие течей жидкостей");
        Epic epic1 = new Epic("Эпик №1", "Завести автомобиль");
        Epic epic2 = new Epic("Эпик №2", "Выехать со двора");
        Epic epic3 = new Epic("Эпик №3", "Тестируем статус пустого эпика");
        SubTask subTask1 = new SubTask("Подзадача №1", "Вставить ключ в замок зажигания");
        SubTask subTask2 = new SubTask("Подзадача №2", "Установить коробку передач на нейтраль");
        SubTask subTask3 = new SubTask("Подзадача №3", "Повернуть ключ в замке зажигания");
        SubTask subTask4 = new SubTask("Подзадача №4", "Пристегнуть ремень безопасности");
        SubTask subTask5 = new SubTask("Подзадача №5", "Открыть ворота парковки");
        SubTask subTask6 = new SubTask("Подзадача №6", "Установить коробку передач на первую передачу");
        SubTask subTask7 = new SubTask("Подзадача №7", "Отпустить сцепление");
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);
        taskManager.addTask(task4);
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        taskManager.addEpic(epic3);
        taskManager.addSubTask(epic1.getTaskId(), subTask1);
        taskManager.addSubTask(epic1.getTaskId(), subTask2);
        taskManager.addSubTask(epic1.getTaskId(), subTask3);
        taskManager.addSubTask(epic2.getTaskId(), subTask4);
        taskManager.addSubTask(epic2.getTaskId(), subTask5);
        taskManager.addSubTask(epic2.getTaskId(), subTask6);
        taskManager.addSubTask(epic2.getTaskId(), subTask7);
        System.out.println("- Задачи/подзадачи/эпики сохранены -");

        System.out.println();
        System.out.println("- Получение списка всех задач -");
        taskManager.printListAllTasks();

        System.out.println();
        System.out.println("- Получение задачи/подзадачи/эпика по идентификатору -");
        taskManager.getTask(task2.getTaskId());
        taskManager.getTask(task4.getTaskId());
        taskManager.getEpic(epic2.getTaskId());
        taskManager.getSubTask(subTask1.getTaskId());
        taskManager.getSubTask(subTask5.getTaskId());
        taskManager.getSubTask(subTask6.getTaskId());


        System.out.println();
        System.out.println("- Обновление задачи/подзадачи/эпика -");
        task2 = new Task(task2.getTaskId(), "Задача №2", "Проверить давление в шинах", TaskStatus.DONE);
        task3 = new Task(task3.getTaskId(), "Задача №3", "Проверить состояние стеклоочистителей", TaskStatus.IN_PROGRESS);
        subTask2 = new SubTask(subTask2.getTaskId(), "Подзадача №2", "Установить коробку передач на нейтраль", TaskStatus.DONE);
        subTask4 = new SubTask(subTask4.getTaskId(), "Подзадача №4", "Пристегнуть ремень безопасности", TaskStatus.DONE);
        subTask5 = new SubTask(subTask5.getTaskId(), "Подзадача №5", "Открыть ворота парковки", TaskStatus.DONE);
        subTask6 = new SubTask(subTask6.getTaskId(), "Подзадача №6", "Установить коробку передач на первую передачу", TaskStatus.DONE);
        epic2 = new Epic(epic2.getTaskId(), "Эпик №2", "Выехать с парковки"); // В эпике статус вручную не меняется.
        taskManager.updTask(task2);
        taskManager.updTask(task3);
        taskManager.updSubTask(subTask2);
        taskManager.updSubTask(subTask4);
        taskManager.updSubTask(subTask5);
        taskManager.updSubTask(subTask6);
        taskManager.updEpic(epic2);
        taskManager.printListAllTasks();

        System.out.println();
        System.out.println("- Получение списка всех подзадач определённого эпика -");
        taskManager.printListSubTaskInEpic(epic2.getTaskId());



        System.out.println();
        System.out.println("- Удаляем подзадачу №7 со статусом NEW в эпике №2. Проверяем, что эпик перешёл в статус DONE -");
        taskManager.delSubTask(subTask7.getTaskId());
        taskManager.getEpic(epic2.getTaskId());


        System.out.println();
        System.out.println("- Удаление задачи/подзадачи/эпика по идентификатору. Вывод списка оставшихся задач -");
        taskManager.delTask(task2.getTaskId());
        taskManager.delTask(task3.getTaskId());
        taskManager.delSubTask(subTask1.getTaskId());
        taskManager.delSubTask(subTask5.getTaskId());
        taskManager.delEpic(epic2.getTaskId());
        taskManager.printListAllTasks();

        System.out.println();
        System.out.println("- Удаление списка задач из трекера -");
        taskManager.delAllTasks();

        System.out.println();
        System.out.println("- Получение списка всех задач -");
        taskManager.printListAllTasks();

    }

}
