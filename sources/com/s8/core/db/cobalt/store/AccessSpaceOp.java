package com.s8.core.db.cobalt.store;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import com.s8.core.arch.silicon.async.MthProfile;
import com.s8.core.arch.titanium.databases.RequestDbMgOperation;
import com.s8.core.arch.titanium.handlers.h3.ConsumeResourceMgAsyncTask;
import com.s8.core.db.cobalt.entry.MgSpaceHandler;
import com.s8.meta.api.flow.S8User;
import com.s8.meta.api.flow.space.AccessSpaceS8Request;
import com.s8.meta.api.flow.space.AccessSpaceS8Response;
import com.s8.meta.api.flow.space.AccessSpaceS8Response.Status;

/**
 * 
 * @author pierreconvert
 *
 */
class AccessSpaceOp extends RequestDbMgOperation<SpaceMgStore> {




	/**
	 * space-handler
	 */
	public final SpaceMgDatabase spaceHandler;



	/**
	 * space-id
	 */
	public final AccessSpaceS8Request request;
	
	
	public final CompletableFuture<AccessSpaceS8Response> future;


	/**
	 * 
	 * @param handler
	 * @param onProcessed
	 * @param onFailed
	 */
	public AccessSpaceOp(long timestamp, S8User initiator, SpaceMgDatabase handler, AccessSpaceS8Request request, CompletableFuture<AccessSpaceS8Response> future) {
		super(timestamp, initiator);
		this.spaceHandler = handler;
		this.request = request;
		this.future = future;
	}


	@Override
	public SpaceMgDatabase getHandler() {
		return spaceHandler;
	}


	@Override
	public ConsumeResourceMgAsyncTask<SpaceMgStore> createAsyncTask() {
		return new ConsumeResourceMgAsyncTask<SpaceMgStore>(spaceHandler) {


			@Override
			public MthProfile profile() { 
				return MthProfile.IO_SSD; 
			}

			@Override
			public String describe() {
				return "ACCESS-EXPOSURE on "+handler.getName()+ " repository";
			}

			@Override
			public boolean consumeResource(SpaceMgStore store) throws IOException {


				MgSpaceHandler spaceHandler = store.getSpaceHandler(request.spaceId);

				if(spaceHandler != null) {
					/* exit point 1 -> continue */
					spaceHandler.accessSpace(timeStamp, initiator, request, future);
				}
				else {
					
					/* exit point 2 -> soft fail */
					future.complete(new AccessSpaceS8Response(Status.SPACE_DOES_NOT_EXIST, null));
					
				}

				/* no new space created */
				return false;
			}

			@Override
			public void catchException(Exception exception) {
				
				/* exit point 3 -> hard fail */
				future.complete(new AccessSpaceS8Response(Status.INTERNAL_ERROR, null));
			}
		};
	}

}
