package com.s8.core.db.cobalt.entry;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.s8.api.flow.S8User;
import com.s8.api.flow.space.requests.AccessSpaceS8Request;
import com.s8.api.flow.space.requests.ExposeSpaceS8Request;
import com.s8.core.arch.magnesium.handlers.h3.H3MgHandler;
import com.s8.core.arch.magnesium.handlers.h3.H3MgIOModule;
import com.s8.core.arch.silicon.SiliconChainCallback;
import com.s8.core.arch.silicon.SiliconEngine;
import com.s8.core.bohr.lithium.branches.LiBranch;
import com.s8.core.db.cobalt.store.SpaceMgStore;


/**
 * 
 * @author pierreconvert
 *
 */
public class MgSpaceHandler extends H3MgHandler<LiBranch> {

	
	/**
	 * 
	 */
	public final static String DATA_FILENAME = "branch-data.li";
	
	
	/**
	 * 
	 */
	private final SpaceMgStore store;
	
	
	/**
	 * 
	 */
	private final IOModule ioModule = new IOModule(this);
	
	
	/**
	 * 
	 */
	private final String id;
	
	/**
	 * 
	 */
	private final Path folderPath;
	
	
	
	/**
	 * 
	 * @param ng
	 * @param store
	 * @param id
	 * @param folderPath
	 */
	public MgSpaceHandler(SiliconEngine ng, SpaceMgStore store, String id, Path folderPath, boolean isSaved) {
		super(ng, isSaved);
		this.store = store;
		this.id = id;
		this.folderPath = folderPath;
	}

	@Override
	public String getName() {
		return "workspace hanlder";
	}

	@Override
	public H3MgIOModule<LiBranch> getIOModule() {
		return ioModule;
	}

	@Override
	public List<H3MgHandler<?>> getSubHandlers() {
		return new ArrayList<>(); // no subhandler
	}

	public Path getFolderPath() {
		return folderPath;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public Path getDataFilePath() {
		return folderPath.resolve(DATA_FILENAME);
	}
	

	public SpaceMgStore getStore() {
		return store;
	}

	public String getIdentifier() {
		return id;
	}
	
	
	
	
	
	

	
	/**
	 * 
	 * @param t
	 * @param onSucceed
	 * @param onFailed
	 */
	public void accessSpace(long t, S8User initiator, SiliconChainCallback callback, AccessSpaceS8Request request) {
		pushOpLast(new AccessSpaceOp(t, initiator, callback, this, request));
	}
	
	
	/**
	 * 
	 * @param t
	 * @param onSucceed
	 * @param onFailed
	 */
	public void exposeObjects(long t, S8User initiator, SiliconChainCallback callback, ExposeSpaceS8Request request) {
		pushOpLast(new ExposeObjectsOp(t, initiator, callback, this, request));
	}


}
