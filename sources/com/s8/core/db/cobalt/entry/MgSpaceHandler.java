package com.s8.core.db.cobalt.entry;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.s8.bohr.io.lithium.branches.LiBranch;
import com.s8.core.arch.silicon.SiliconEngine;
import com.s8.core.arch.titanium.handlers.h3.H3MgHandler;
import com.s8.core.arch.titanium.handlers.h3.H3MgIOModule;
import com.s8.core.db.cobalt.store.SpaceMgStore;
import com.s8.meta.api.flow.S8User;
import com.s8.meta.api.flow.space.AccessSpaceS8Request;
import com.s8.meta.api.flow.space.AccessSpaceS8Response;
import com.s8.meta.api.flow.space.ExposeSpaceS8Request;
import com.s8.meta.api.flow.space.ExposeSpaceS8Response;


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
	public void accessSpace(long t, S8User initiator, AccessSpaceS8Request request, CompletableFuture<AccessSpaceS8Response> future) {
		pushOpLast(new AccessSpaceOp(t, initiator, this, request, future));
	}
	
	
	/**
	 * 
	 * @param t
	 * @param onSucceed
	 * @param onFailed
	 */
	public void exposeObjects(long t, S8User initiator, ExposeSpaceS8Request request, CompletableFuture<ExposeSpaceS8Response> future) {
		pushOpLast(new ExposeObjectsOp(t, initiator, this, request, future));
	}


}
