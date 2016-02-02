package com.javarush.test.level33.lesson15.big01.strategies;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileBucket {


    private Path path;

    public FileBucket() {
        try {
            path = Files.createTempFile("tmp", null, null);
        }
        catch (IOException e) {}
        path.toFile().deleteOnExit();
    }

    //он должен возвращать размер файла на который указывает path
    public long getFileSize() {
        return path.toFile().length();
    }

    //должен сериализовывать переданный entry в файл. Учти, каждый entry может содержать еще один entry
    public void putEntry(Entry entry) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(Files.newOutputStream(path))) {
            Entry e = entry;
            while (e != null) {
                objectOutputStream.writeObject(e);
                e = e.next;
            }
        }
        catch (IOException e) {}
    }



    //должен забирать entry из файла. Если файл имеет нулевой размер, вернуть null
    public Entry getEntry() {
        if (getFileSize() == 0) return null;

        try (ObjectInputStream s = new ObjectInputStream(Files.newInputStream(path))) {
            Entry result = (Entry) s.readObject();
            Entry last = result;
            Entry temp;
            while ((temp = (Entry)s.readObject()) != null) {
                last.next = temp;
                last = temp;
            }
            return result;
        }
        catch (IOException | ClassNotFoundException e) {
            return null;
        }

    }
    //удалять файл на который указывает path
    public void remove() {
        try {
            Files.delete(path);
        } catch (IOException e) {}
    }




}

/*
Задание 9.

Напишем еще одну стратегию, назовем ее FileStorageStrategy. Она будет очень похожа
на стратегию OurHashMapStorageStrategy, но в качестве ведер (англ. buckets) будут
файлы. Я знаю, ты знаешь о каких buckets идет речь, если нет – повтори внутреннее
устройство HashMap.
9.1.	Создай класс FileBucket в пакете strategies.
9.2.	Добавь в класс поле Path path. Это будет путь к файлу.
9.3.	Добавь в класс конструктор, он должен:
9.3.1.	Инициализировать path временным файлом. Файл должен быть размещен
в директории для временных файлов и иметь случайное имя. Подсказка:
Files.createTempFile.
9.3.2.	Создавать новый файл, используя path. Если такой файл уже есть, то
заменять его.
9.3.3.	Обеспечивать удаление файла при выходе из программы. Подсказка:
deleteOnExit().
9.4.	Добавь в класс методы:
9.4.1.	long getFileSize(), он должен возвращать размер файла на который
указывает path.
9.4.2.	void putEntry(Entry entry) - должен сериализовывать переданный entry в
файл. Учти, каждый entry может содержать еще один entry.
9.4.3.	Entry getEntry() - должен забирать entry из файла. Если файл имеет нулевой
размер, вернуть null.
9.4.4.	void remove() – далять файл на который указывает path.
Конструктор и методы не должны кидать исключения.
 */