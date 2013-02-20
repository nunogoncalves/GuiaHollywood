package com.numicago.guiahollywood.objects;

public enum EnumActorSort {
	NAME(UserPreferences.SORT_ACTOR_BY_NAME, "actorName"), 
	AGE (UserPreferences.SORT_ACTOR_BY_AGE, "actorAge"), 
	N_MOVIES (UserPreferences.SORT_ACTOR_BY_N_MOVIES, "actorNMovies");
	
	int id;
	
	String name;

	private EnumActorSort(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}


	public static int getIdFromSortName(String name) {
		for (EnumActorSort sort : EnumActorSort.values()) {
			if (sort.getName().equals(name)) {
				return sort.getId();
			}
		}
		return 0;
	}
}
