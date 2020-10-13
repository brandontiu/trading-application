package Transactions;

import java.io.Serializable;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;

public class Meeting implements Serializable {
    /**
     * <h1>Meeting</h1>
     *
     * This class represents a meeting among 2 users.<br>
     * <br><br>
     * Variables: <br>
     *
     * user1approved: Keeps track of whether the user has confirmed the details of the meeting.
     *
     * user2approved: explained above.
     *
     * numEdits: this stores the number of times that both users have made an edit to the program (check phase 1
     * if you disagree.) example: user1 and user2 need to both edit this meeting in order for numedits to increase to 1
     * from 0.
     *
     * maxNumEdits: this stores the maximum number of edits that can be done by both users before the transaction is
     * cancelled.
     *
     * userEditable: This is a mapping of all the information that the user can change. This contains capitalized
     * strings mapping to the information they change.
     * <br><br>
     * -- userEditable Variables -- <br>
     * Time, Date, Location. (will be stored in the hashmap)
     * NOTE: getters and setters for certain information still remain camel-case.
     */
    private HashMap<Integer, Integer> numUserEdits = new HashMap<>();
    private int maxNumEdits = 3;
    private String location;
    private Date meetingDate;
    private Date meetingTime;

    /**
     * The constructor for the class Transactions.Meeting.
     * @param location location to be stored in usereditable hashmap.
     * @param meetingTime The time of the meetup as a localTime object.
     * @param meetingDate the dateof the meetup as a  LocalDate object.
     */
    public Meeting(String location, Date meetingTime, Date meetingDate){
        this.location = location;
        this.meetingDate = meetingDate;
        this.meetingTime = meetingTime;
        numUserEdits.put(1, 0);
        numUserEdits.put(2, 0);
    }

    /**
     * Getter for number of edits for this particular meeting by User1.
     * @return returns the number of times when user1 has edited a meeting.
     */
    public int getNumEditsUser(Integer num){
        return numUserEdits.get(num);
    }


    /**
     * This is a getter for location.
     * @return this returns the value of location for the meeting.
     */
    public String getLocation(){
        return(this.location);
    }

    /**
     * This is a getter for the time of the meeting.
     * @return this returns an instance for the time of the meeting.
     */
    public Date getTime(){
        return(this.meetingTime);
    }

    /**
     * This is a getter for the date of the meeting.
     * @return this returns the instance of the date for the meeting.
     */
    public Date getDate(){
        return(this.meetingDate);
    }

    /**
     * Setter for the location variable in the meeting class.
     * @param newLocation the new location.
     */
    public void setLocation(String newLocation){
        this.location = newLocation;
    }

    /**
     * This is a setter for the date of the transaction.
     * @param NewDate this is an instance of the new date object.
     */
    public void setDate(Date NewDate) {
        this.meetingDate =  NewDate;
    }

    /**
     * This is a setter for the time of the transaction.
     * @param NewTime the hour of the time to be set.
     */
    public void setTime(Date NewTime){
        this.meetingTime = NewTime;
    }

    /**
     * this method is called when user1 edits the meeting, his number of edit increase by one
     */
    public void userEdits(int num){
        int currEdits = numUserEdits.get(num);
        numUserEdits.replace(num, currEdits + 1);}

    /**
     * Getter for number of maxNumeditsUser1 for this particular meeting.
     * @return returns the number of times both users are allowed to edit a meeting.
     */
    public int getMaxNumEdits() {
        return maxNumEdits;
    }

    @Override
    public String toString(){
        return("Meeting on " + this.getDate().toString())+ " at " + location;
    }
}

