package hanas.dnidomatury.model.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ExamDAO {
    @Insert
    public void insert(ExamDB... items);

    @Update
    public void update(ExamDB... items);

    @Delete
    public void delete(ExamDB item);

    @Query("SELECT * FROM exams")
    public List<ExamDB> getExams();

    @Query("SELECT * FROM exams WHERE id = :id")
    public ExamDB getExamById(Long id);

    @Query("SELECT * FROM exams WHERE selectedOrder ORDER BY selectedOrder")
    public List<ExamDB> getSelectedExams();

    @Query("SELECT * FROM exams WHERE name = :name AND level = :level AND type = :type")
    public ExamDB findExam(String name, String level, String type);

    @Query("UPDATE exams SET selectedOrder = (SELECT COUNT(selectedOrder) FROM exams) + 1 WHERE id = :id")
    public void selectExam(Long id);

    @Query("UPDATE exams SET selectedOrder = Null WHERE id = :id")
    public void deselectExam(Long id);

    @Query("UPDATE exams SET selectedOrder = CASE " +
            "WHEN (:oldPosition < :newPosition AND NOT selectedOrder = :oldPosition) THEN selectedOrder - 1 " +
            "WHEN (:oldPosition > :newPosition AND NOT selectedOrder = :oldPosition) THEN selectedOrder + 1 " +
            "ELSE :newPosition END " +
            "WHERE selectedOrder BETWEEN :oldPosition AND :newPosition OR selectedOrder BETWEEN :newPosition AND :oldPosition" )
    public void moveSelectedExam(Integer oldPosition, Integer newPosition);



}

