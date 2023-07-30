package com.project.musicrecommendation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Interactive menu where the user can use the functionalities of the
 * MusicRecommender
 *
 * @version 2
 * @author
 */
public class MusicRecommender {

    String musicListFileName;
    List<String> songs = new ArrayList<String>();

    MusicRecommender(String musicListFileName) {
	this.musicListFileName = musicListFileName;
	addSongs();
    }

    public void addSongs() {
	BufferedReader br = null;
	try {
	    br = new BufferedReader(new FileReader(
		    "C:\\Users\\DELL\\eclipse-workspace\\musicreco\\src\\com\\project\\musicrecommendation\\Music_list.txt"));
	    String line;
	    while ((line = br.readLine()) != null) {
		songs.add(line);
	    }
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} finally {
	    try {
		br.close();
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
    }

    private ArrayList<Music> searchArtists(MusicProfile musicProfile) throws NoRecommendationException {
	// TODO Auto-generated method stub
	String preferredArtist = musicProfile.getPreferredArtist().toLowerCase();
	ArrayList<Music> searchResult = new ArrayList<Music>();
	for (int i = 0; i < songs.size(); i++) {
	    String arr[] = songs.get(i).split(" ");
	    String trackName = arr[0];
	    String artist = arr[1].toLowerCase();
	    String genre = arr[2];
	    int BPM = Integer.parseInt(arr[3]);
	    int popularity = Integer.parseInt(arr[4]);

	    if (artist.indexOf(preferredArtist) != -1) {
		Music m = new Music(trackName, artist, genre, BPM, popularity + 1);
		searchResult.add(m);
		songs.set(i, trackName + " " + artist + " " + genre + " " + BPM + " " + (popularity + 1));
	    }
	}
	if (searchResult.isEmpty()) {
	    throw new NoRecommendationException("No recommendation for this Artist");
	}

	return searchResult;
    }

    private Object genreBasedRecommendation(MusicProfile musicProfile) throws NoRecommendationException {
	String genreBase = musicProfile.getPreferredGenre();
	Music m = null;
	for (int i = 0; i < songs.size(); i++) {
	    String arr[] = songs.get(i).split(" ");
	    String trackName = arr[0];
	    String artist = arr[1];
	    String genre = arr[2];
	    int BPM = Integer.parseInt(arr[3]);
	    int popularity = Integer.parseInt(arr[4]);

	    if (genreBase.equalsIgnoreCase(genre)) {
		m = new Music(trackName, artist, genre, BPM, popularity + 1);
		songs.set(i, trackName + " " + artist + " " + genre + " " + BPM + " " + (popularity + 1));
	    }
	}
	if (m == null) {
	    throw new NoRecommendationException("No recommendation for this genre");
	}
	return m;
    }

    private Music getMostPopularMusic() {
	int max = 0;
	Music m = null;
	int index = 0;
	for (int i = 0; i < songs.size(); i++) {
	    String arr[] = songs.get(i).split(" ");
	    String trackName = arr[0];
	    String artist = arr[1];
	    String genre = arr[2];
	    int BPM = Integer.parseInt(arr[3]);
	    int popularity = Integer.parseInt(arr[4]);

	    if (max < popularity) {
		max = popularity;
		index = i;
	    }
	}
	{
	    String arr[] = songs.get(index).split(" ");
	    String trackName = arr[0];
	    String artist = arr[1];
	    String genre = arr[2];
	    int BPM = Integer.parseInt(arr[3]);
	    int popularity = Integer.parseInt(arr[4]);

	    m = new Music(trackName, artist, genre, BPM, popularity + 1);
	    songs.set(index, trackName + " " + artist + " " + genre + " " + BPM + " " + (popularity + 1));
	}

	return m;

    }

    private Object BPMBasedRecommendation(MusicProfile musicProfile) throws NoRecommendationException {
	Music m = null;
	int prefferedBPM = musicProfile.getPreferredBPM();
	for (int i = 0; i < songs.size(); i++) {
	    String arr[] = songs.get(i).split(" ");
	    String trackName = arr[0];
	    String artist = arr[1];
	    String genre = arr[2];
	    int BPM = Integer.parseInt(arr[3]);
	    int popularity = Integer.parseInt(arr[4]);

	    if (prefferedBPM == BPM) {
		m = new Music(trackName, artist, genre, BPM, popularity + 1);
		songs.set(i, trackName + " " + artist + " " + genre + " " + BPM + " " + (popularity + 1));
	    }
	}
	{

	}
	if (m == null) {
	    throw new NoRecommendationException("No recommendation for this genre");
	}
	return m;

    }

    private void saveMusicList() {
	try (BufferedWriter writer = new BufferedWriter(new FileWriter(
		"C:\\\\Users\\\\DELL\\\\eclipse-workspace\\\\musicreco\\\\src\\\\com\\\\project\\\\musicrecommendation\\\\Music_list.txt"))) {
	    for (String element : songs) {
		writer.write(element);
		writer.newLine();
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	}

    }

    public static MusicRecommender createMusicRecommender(String musicListFileName)
	    throws FileNotFoundException, MusicFileFormatException {
	MusicRecommender musicRecommender = new MusicRecommender(musicListFileName);
	return musicRecommender;

    }

    /**
     * Interactive menu where the user can use the functionalities of the
     * MusicRecommender
     * 
     * @param args No argument is needed for this main method
     */
    public static void main(String[] args) {
	Scanner scan = new Scanner(System.in);
	MusicRecommender musicRecommender = null;
	while (true) {
	    try {
		System.out.println("Welcome! What's the name of the file containing the music list?");
		String musicListFileName = scan.nextLine();

		musicRecommender = MusicRecommender.createMusicRecommender(musicListFileName);
		break;
	    } catch (FileNotFoundException e) {
		System.out.println("The file does not exist! Please enter a valid file name!");
	    } catch (MusicFileFormatException e) {
		System.out.println(e.getMessage());
		return;
	    }
	}
	System.out.println("What's the name of the user?");
	String name = scan.nextLine();
	System.out.println("Who's your favorite artist?");
	String artist = scan.nextLine();
	System.out.println("What's your favorite genre?");
	String genre = scan.nextLine();
	System.out.println("What's your preferred BPM?");
	int BPM = scan.nextInt();
	System.out.println("Do you like popular music? (Y/N)");
	scan.nextLine();
	boolean likePopular = (scan.nextLine().equalsIgnoreCase("Y"));
	MusicProfile musicProfile = new MusicProfile(name, artist, genre, BPM, likePopular);

	System.out.printf("Hi %s!\n", musicProfile.getName());
	int option = -1;
	while (option != 5) {
	    System.out.println(
		    "1. Find songs of my favorite artists\n" + "2. Get a recommendation based on my preferred genre\n"
			    + "3. Get a recommendation based on my preferred BPM\n"
			    + "4. Show me the most popular song\n" + "5. Exit");
	    boolean wrongCondition;
	    do {
		option = scan.nextInt();
		wrongCondition = ((option != 1) && (option != 2) && (option != 3) && (option != 4) && (option != 5));
		if (wrongCondition)
		    System.out.println("Please enter a valid option!");
	    } while (wrongCondition);
	    switch (option) {
	    case 1 -> {
		try {
		    ArrayList<Music> searchResult = musicRecommender.searchArtists(musicProfile);
		    System.out.printf("Listing songs by %s:\n", musicProfile.getPreferredArtist());
		    for (Music music : searchResult) {
			System.out.println(music.toString());
		    }
		} catch (NoRecommendationException e) {
		    System.out.println(e.getMessage());
		}
	    }
	    case 2 -> {
		try {
		    System.out.println(musicRecommender.genreBasedRecommendation(musicProfile).toString());
		} catch (NoRecommendationException e) {
		    System.out.println(e.getMessage());
		}
	    }
	    case 3 -> {
		try {
		    System.out.println(musicRecommender.BPMBasedRecommendation(musicProfile).toString());
		} catch (NoRecommendationException e) {
		    System.out.println(e.getMessage());
		}
	    }
	    case 4 -> System.out.println(musicRecommender.getMostPopularMusic().toString());
	    case 5 -> {
	    }
	    }
	}
	musicRecommender.saveMusicList();
	System.out.println("Thanks for using the music recommender!");
    }

}