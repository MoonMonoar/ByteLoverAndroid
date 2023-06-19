package com.immo2n.bytelover;

/*
>>This class takes global object to keep track of context
and to use the other methods necessary to function the app.
It also uses the net class that handles network queries.
This class actually queries data from the server using net's
post, get and data methods, also it can cache the reposes.
>> Net class returns an object that has non re-usable methods, so
its not passed directly.
*/
public class Server {
    private Global global;
    public Server(Global global_object){
        this.global = global_object;
    }
    public final String server = "https://api.bytelover.com/android";

    //DATA LINKS
    public String courses_data_link = server+"/data/courses.php";
    public String getCourses_data_link() {
        return courses_data_link;
    }
}
