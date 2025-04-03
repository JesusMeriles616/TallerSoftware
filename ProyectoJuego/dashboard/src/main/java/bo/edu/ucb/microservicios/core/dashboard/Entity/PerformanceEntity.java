package bo.edu.ucb.microservicios.core.dashboard.Entity;

import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "performance")
public class PerformanceEntity {

    @Id
    private String id = UUID.randomUUID().toString();

    @Column(name = "child_id", nullable = false)
    private String childId;

    @Column(name = "game_id", nullable = false)
    private String gameId;

    @Column(name = "correct_answers")
    private int correctAnswers;

    @Column(name = "wrong_answers")
    private int wrongAnswers;

    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date = new Date();

    // Constructor vacío para JPA
    public PerformanceEntity() {}

    public PerformanceEntity(String childId, String gameId, int correctAnswers, int wrongAnswers) {
        this.childId = childId;
        this.gameId = gameId;
        this.correctAnswers = correctAnswers;
        this.wrongAnswers = wrongAnswers;
    }

    public String getId() { return id; }
    public String getChildId() { return childId; }
    public String getGameId() { return gameId; }
    public int getCorrectAnswers() { return correctAnswers; }
    public int getWrongAnswers() { return wrongAnswers; }
    public Date getDate() { return date; }

    public void setChildId(String childId) { this.childId = childId; }
    public void setGameId(String gameId) { this.gameId = gameId; }
    public void setCorrectAnswers(int correctAnswers) { this.correctAnswers = correctAnswers; }
    public void setWrongAnswers(int wrongAnswers) { this.wrongAnswers = wrongAnswers; }
    public void setDate(Date date) { this.date = date; }
}
