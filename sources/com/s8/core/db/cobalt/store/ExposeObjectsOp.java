package com.s8.core.db.cobalt.store;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import com.s8.core.arch.silicon.async.MthProfile;
import com.s8.core.arch.titanium.databases.RequestDbMgOperation;
import com.s8.core.arch.titanium.handlers.h3.ConsumeResourceMgAsyncTask;
import com.s8.core.db.cobalt.entry.MgSpaceHandler;
import com.s8.meta.api.flow.S8User;
import com.s8.meta.api.flow.space.ExposeSpaceS8Request;
import com.s8.meta.api.flow.space.ExposeSpaceS8Response;
import com.s8.meta.api.flow.space.ExposeSpaceS8Response.Status;

/**
 * 
 * @author pierreconvert
 *
 */
class ExposeObjectsOp extends RequestDbMgOperation<SpaceMgStore> {

	/**
	 * 
	 */
	public final SpaceMgDatabase spaceHandler;


	public final ExposeSpaceS8Request request;
	
	
	public final CompletableFuture<ExposeSpaceS8Response> future;



	/**
	 * 
	 * @param branchHandler
	 * @param onSucceed
	 * @param onFailed
	 */
	public ExposeObjectsOp(long timestamp, S8User initiator, SpaceMgDatabase spaceHandler, ExposeSpaceS8Request request,
			CompletableFuture<ExposeSpaceS8Response> future) {
		super(timestamp, initiator);
		this.spaceHandler = spaceHandler;
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
				return MthProfile.FX0; 
			}

			@Override
			public String describe() {
				return "ACCESS-EXPOSURE on "+spaceHandler.getName()+ " repository";
			}

			@Override
			public boolean consumeResource(SpaceMgStore store) throws IOException {

				MgSpaceHandler spaceHandler = store.getSpaceHandler(request.spaceId);
				if(spaceHandler != null) {
					
					spaceHandler.exposeObjects(timeStamp, initiator, request, future);

					/* not change in the db itself, despite space will be modified */
					return false;
				}
				else {
					
					future.complete(new ExposeSpaceS8Response(Status.NOT_FOUND, 0x0));
					
					return false;
				}
			}


			@Override
			public void catchException(Exception exception) {
				future.complete(new ExposeSpaceS8Response(Status.INTERNAL_EROR, 0x0));
			}
		};
	}

}
