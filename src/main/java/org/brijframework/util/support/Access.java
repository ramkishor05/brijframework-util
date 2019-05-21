package org.brijframework.util.support;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public enum Access {
	NONE(0x000),
	//PUBLIC(0x001),
	//PROTECTED(0x002),
	//PRIVATE(0x004),
	FINAL(0x008),
	STATIC(0x010),
	ABSTRACT(0x020),
	NATIVE(0x040),
	SYNCHRONIZED(0x080),
	TRANSIENT(0x100),
	VOLATILE(0x200),
	
	DEFAULT(0), 
	DEFAULT_NO_STATIC(8),
	DEFAULT_NO_FINAL(16),
	DEFAULT_NO_STATIC_FINAL(24),
	DEFAULT_NO_TRANSIENT(128),
	DEFAULT_NO_STATIC_TRANSIENT(136),
	
	PRIVATE(2),
	PRIVATE_NO_STATIC(10),
	PRIVATE_NO_FINAL(18),
	PRIVATE_NO_STATIC_FINAL(26),
	PRIVATE_NO_TRANSIENT(132),
	PRIVATE_NO_STATIC_TRANSIENT(140),
	
	PROTECTED(4), 
	PROTECTED_NO_STATIC(12),
	PROTECTED_NO_FINAL(20), 
	PROTECTED_NO_STATIC_FINAL(28), 
	PROTECTED_NO_TRANSIENT(132),
	PROTECTED_NO_STATIC_TRANSIENT(142),
	
	PUBLIC(1), 
	PUBLIC_NO_STATIC(9),
	PUBLIC_NO_FINAL(17),
	PUBLIC_NO_STATIC_FINAL(25),
	PUBLIC_NO_TRANSIENT(129),
	PUBLIC_NO_STATIC_TRANSIENT(137);
	
	int id;

	private Access(int ID) {
		this.id = ID;
	}

	private static LinkedHashMap<Integer, java.util.List<Integer>> groupmap = new LinkedHashMap<>();

	static {
		
		List<Integer> publicGroup = new LinkedList<>();
		// inculde public
		publicGroup.add(1); // public NO
		publicGroup.add(9); // public STATIC
		publicGroup.add(17); // public FINAL
		publicGroup.add(25); // public FINAL_STATIC
		publicGroup.add(65); // public VOLATILE
		publicGroup.add(129); // public TRANSIENT
		publicGroup.add(137); // public STATIC TRANSIENT
		
		groupmap.put(1, publicGroup);
		//remove static
		groupmap.put(9, removeValues(publicGroup, PUBLIC_NO_STATIC.getID(),PUBLIC_NO_STATIC_FINAL.getID(),PUBLIC_NO_STATIC_TRANSIENT.getID()));
		//remove final
		groupmap.put(17, removeValues(publicGroup, PUBLIC_NO_FINAL.getID(),PUBLIC_NO_STATIC_FINAL.getID()));
		//remove final static or final static
		groupmap.put(25, removeValues(publicGroup, PUBLIC_NO_STATIC_FINAL.getID()));
		//remove transient
		groupmap.put(129, removeValues(publicGroup, PUBLIC_NO_TRANSIENT.getID(),PUBLIC_NO_STATIC_TRANSIENT.getID()));
		//remove static transient
		groupmap.put(137, removeValues(publicGroup, PUBLIC_NO_STATIC_TRANSIENT.getID()));
		
		List<Integer> protectedGroup = new LinkedList<>();
		protectedGroup.add(4); // protected NO
		protectedGroup.add(12); // protected STATIC
		protectedGroup.add(20); // protected FINAL
		protectedGroup.add(28); // protected FINAL_STATIC
		protectedGroup.add(68); // protected VOLATILE
		protectedGroup.add(132); // protected TRANSIENT
		protectedGroup.add(140); // protected STATIC TRANSIENT
		// include public
		protectedGroup.addAll(publicGroup);
		
		groupmap.put(4, protectedGroup);
		//remove static
		groupmap.put(12, removeValues(protectedGroup, PROTECTED_NO_STATIC.getID(),PROTECTED_NO_STATIC_FINAL.getID(),PROTECTED_NO_STATIC_TRANSIENT.getID(),PUBLIC_NO_STATIC.getID(), PUBLIC_NO_STATIC_FINAL.getID(),PUBLIC_NO_STATIC_TRANSIENT.getID()));
		//remove final
		groupmap.put(20, removeValues(protectedGroup, PROTECTED_NO_FINAL.getID(),PROTECTED_NO_STATIC_FINAL.getID(),PUBLIC_NO_FINAL.getID(),PUBLIC_NO_STATIC_FINAL.getID()));
		//remove final static or final static
		groupmap.put(28, removeValues(protectedGroup, PROTECTED_NO_STATIC_FINAL.getID(),PUBLIC_NO_STATIC_FINAL.getID()));
		//remove transient
		groupmap.put(132, removeValues(protectedGroup, PROTECTED_NO_TRANSIENT.getID(),PUBLIC_NO_TRANSIENT.getID()));
		//remove static transient
		groupmap.put(142, removeValues(protectedGroup, PROTECTED_NO_STATIC_TRANSIENT.getID(),PUBLIC_NO_STATIC_TRANSIENT.getID()));
		
		List<Integer> defaultGroup = new LinkedList<>();
		defaultGroup.add(0); // default NO
		defaultGroup.add(8); // default STATIC
		defaultGroup.add(16); // default FINAL
		defaultGroup.add(24); // default FINAL_STATIC
		defaultGroup.add(64); // default VOLATILE
		defaultGroup.add(128); // default TRANSIENT
		defaultGroup.add(136); // default STATIC TRANSIENT
		defaultGroup.addAll(protectedGroup);
		groupmap.put(0, defaultGroup);
		//remove static
		groupmap.put(8, removeValues(defaultGroup, DEFAULT_NO_STATIC.getID(),DEFAULT_NO_STATIC_FINAL.getID(),DEFAULT_NO_STATIC_TRANSIENT.getID(),PROTECTED_NO_STATIC.getID(),PROTECTED_NO_STATIC_FINAL.getID(),PROTECTED_NO_STATIC_TRANSIENT.getID(),PUBLIC_NO_STATIC.getID(), PUBLIC_NO_STATIC_FINAL.getID(),PUBLIC_NO_STATIC_TRANSIENT.getID()));
		//remove final
		groupmap.put(16, removeValues(defaultGroup, DEFAULT_NO_FINAL.getID(),DEFAULT_NO_STATIC_FINAL.getID(),PROTECTED_NO_FINAL.getID(),PROTECTED_NO_STATIC_FINAL.getID(),PUBLIC_NO_FINAL.getID(),PUBLIC_NO_STATIC_FINAL.getID()));
		//remove final static or final static
		groupmap.put(24, removeValues(defaultGroup, DEFAULT_NO_STATIC_FINAL.getID(),PROTECTED_NO_STATIC.getID(),PUBLIC_NO_STATIC.getID()));
		//remove transient
		groupmap.put(128, removeValues(defaultGroup, DEFAULT_NO_TRANSIENT.getID(),PROTECTED_NO_STATIC.getID(),PUBLIC_NO_STATIC.getID()));
		//remove static transient
		groupmap.put(136, removeValues(defaultGroup, DEFAULT_NO_STATIC_TRANSIENT.getID(), PROTECTED_NO_STATIC_TRANSIENT.getID(),PUBLIC_NO_STATIC_TRANSIENT.getID()));
		List<Integer> privateGroup = new LinkedList<>();
		privateGroup.add(2); // private NO
		privateGroup.add(10); // private STATIC
		privateGroup.add(18); // private FINAL
		privateGroup.add(26); // private FINAL_STATIC
		privateGroup.add(68); // private VOLATILE
		privateGroup.add(130); // private TRANSIENT
		privateGroup.addAll(defaultGroup);
		groupmap.put(2, privateGroup);
		//remove static
		groupmap.put(10, removeValues(privateGroup, PRIVATE_NO_STATIC.getID(), PRIVATE_NO_STATIC_FINAL.getID(),PRIVATE_NO_STATIC_TRANSIENT.getID(),DEFAULT_NO_STATIC.getID(),DEFAULT_NO_STATIC_FINAL.getID(),DEFAULT_NO_STATIC_TRANSIENT.getID(),PROTECTED_NO_STATIC.getID(),PROTECTED_NO_STATIC_FINAL.getID(),PROTECTED_NO_STATIC_TRANSIENT.getID(),PUBLIC_NO_STATIC.getID(), PUBLIC_NO_STATIC_FINAL.getID(),PUBLIC_NO_STATIC_TRANSIENT.getID()));
		//remove final
		groupmap.put(16, removeValues(privateGroup, PRIVATE_NO_FINAL.getID(), PRIVATE_NO_STATIC_FINAL.getID(), DEFAULT_NO_FINAL.getID(),DEFAULT_NO_STATIC_FINAL.getID(),PROTECTED_NO_FINAL.getID(),PROTECTED_NO_STATIC_FINAL.getID(),PUBLIC_NO_FINAL.getID(),PUBLIC_NO_STATIC_FINAL.getID()));
		
		groupmap.put(26, removeValues(privateGroup, PRIVATE_NO_FINAL.getID(), PRIVATE_NO_STATIC_FINAL.getID(), DEFAULT_NO_FINAL.getID(),DEFAULT_NO_STATIC_FINAL.getID(),PROTECTED_NO_FINAL.getID(),PROTECTED_NO_STATIC_FINAL.getID(),PUBLIC_NO_FINAL.getID(),PUBLIC_NO_STATIC_FINAL.getID()));
		//remove final static or final static
		groupmap.put(24, removeValues(privateGroup, PRIVATE_NO_STATIC_FINAL.getID() ,DEFAULT_NO_STATIC_FINAL.getID(),PROTECTED_NO_STATIC.getID(),PUBLIC_NO_STATIC.getID()));
		//remove transient
		groupmap.put(128, removeValues(privateGroup,PRIVATE_NO_TRANSIENT.getID(),DEFAULT_NO_TRANSIENT.getID(),PROTECTED_NO_STATIC.getID(),PUBLIC_NO_STATIC.getID()));
		//remove static transient
		groupmap.put(136, removeValues(privateGroup,PRIVATE_NO_STATIC_TRANSIENT.getID(),DEFAULT_NO_STATIC_TRANSIENT.getID(), PROTECTED_NO_STATIC_TRANSIENT.getID(),PUBLIC_NO_STATIC_TRANSIENT.getID()));
	}
	
	
	public static List<Integer> removeValues(List<Integer> integers , Integer...intArray){
		List<Integer> group = new LinkedList<>();
		HashMap<Integer,Integer> intHash=new HashMap<>();
		for(Integer init:intArray){
			intHash.put(init, init);
		}
		for(Integer value :integers){
			if(intHash.get(value)==null){
				group.add(value);
			}
		}
		return group;
	}

	@Override
	public String toString() {
		return super.toString();
	}

	public boolean isAccess(Integer in) {
		for (Integer id : groupmap.get(this.id)) {
			if (in.equals(id)) {
				return true;
			}
		}
		return false;
	}
	
	public Integer allow(Integer in) {
		for (Integer id : groupmap.get(this.id)) {
			if (in.equals(id)) {
				return id;
			}
		}
		return in;
	}
		
	public Integer getID() {
		return this.id;
	}

}
