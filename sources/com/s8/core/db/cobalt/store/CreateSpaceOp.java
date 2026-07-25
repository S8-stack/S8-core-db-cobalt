package com.s8.core.db.cobalt.store;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import com.s8.bohr.io.lithium.branches.LiBranch;
import com.s8.core.arch.silicon.async.MthProfile;
import com.s8.core.arch.titanium.databases.RequestDbMgOperation;
import com.s8.core.arch.titanium.handlers.h3.ConsumeResourceMgAsyncTask;
import com.s8.core.db.cobalt.entry.MgSpaceHandler;
import com.s8.meta.api.flow.S8User;
import com.s8.meta.api.flow.space.CreateSpaceS8Request;
import com.s8.meta.api.flow.space.CreateSpaceS8Response;
import com.s8.meta.api.flow.space.CreateSpaceS8Response.Status;

/**
 * 
 * @author pierreconvert
 *
 */
class CreateSpaceOp extends RequestDbMgOperation<SpaceMgStore> {




	/**
	 * 
	 */
	public final SpaceMgDatabase spaceHandler;


	/**
	 * 
	 */
	public final CreateSpaceS8Request request;
	
	
	/**
	 * 
	 */
	public final CompletableFuture<CreateSpaceS8Response> future;





	/**
	 * 
	 * @param handler
	 * @param onProcessed
	 * @param onFailed
	 */
	public CreateSpaceOp(long timestamp, S8User initiator, SpaceMgDatabase handler, CreateSpaceS8Request request,
			CompletableFuture<CreateSpaceS8Response> future) {
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


				MgSpaceHandler spaceHandler = store.createSpaceHandler(request.spaceId);

				if(spaceHandler != null) {

					LiBranch branch = new LiBranch(request.spaceId, store.getCodebase());
					branch.expose(request.exposure);

					spaceHandler.initializeResource(branch);
						
					future.complete(new CreateSpaceS8Response(Status.OK, 0x0L));

					return true;
				}
				else {
					
					/* exit point 2 -> soft fail */
					future.complete(new CreateSpaceS8Response(Status.SPACE_ID_CONFLICT, 0x0L));
				
					return false;
				}
			}

			@Override
			public void catchException(Exception exception) {
				
				/* exit point 3 -> hard fail */
				future.complete(new CreateSpaceS8Response(Status.INTERNAL_ERROR, 0x0L));
				
			}
		};
	}

}
