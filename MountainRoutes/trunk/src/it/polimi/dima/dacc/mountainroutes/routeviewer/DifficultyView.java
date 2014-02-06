package it.polimi.dima.dacc.mountainroutes.routeviewer;

import it.polimi.dima.dacc.mountainroutes.R;
import it.polimi.dima.dacc.mountainroutes.types.Difficulty;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Custom view that displays a value of {@link Difficulty} enumeration.
 * @author Chiara
 *
 */
public class DifficultyView extends TextView {

	private Context context;

	public DifficultyView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}

	public DifficultyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public DifficultyView(Context context) {
		super(context);
		this.context = context;
	}

	public void setDifficulty(Difficulty difficulty) {
		String value;

		if(difficulty == null){
			return;
		}
		
		switch (difficulty) {
		case TURISTIC:
			value = context.getString(R.string.difficulty_tourist);
			break;
		case EXCURSIONIST:
			value = context.getString(R.string.difficulty_excursionist);
			break;
		case ADVANCED:
			value = context.getString(R.string.difficulty_advanced);
			break;
		case EQUIPPED:
			value = context.getString(R.string.difficulty_equipped);
			break;
		default:
			return;
		}
		
		setText(value);
	}

}
