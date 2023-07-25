package org.formation.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.formation.model.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

@Repository
@Profile("file")
public class FileDAO implements MovieDao {

	@Value("${movie.file}")
	String file;
	
	private List<Movie> data;
	
	public List<Movie> findAll() {
		try  {
			if ( data == null ) {
				loadFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}


	
	private void loadFile() throws IOException {
		
		data = new ArrayList<Movie>();
		
		InputStream in = this.getClass().getResourceAsStream(file);

		BufferedReader br = new BufferedReader (new InputStreamReader(in));

		String line=null;
		while ((line=br.readLine()) != null) {
			String [] tokens = line.split(";");
			Movie m = new Movie();
			m.setDirector(tokens[0]);
			m.setTitle(tokens[1]);
			m.setDuration(Integer.parseInt(tokens[2]));
			data.add(m);
		}
	}


}
