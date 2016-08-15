package org.revenj.server.commands;

import org.revenj.patterns.*;
import org.revenj.security.PermissionManager;
import org.revenj.server.CommandResult;
import org.revenj.server.ReadOnlyServerCommand;
import org.revenj.serialization.Serialization;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

public class GetDomainObject implements ReadOnlyServerCommand {

	private final DomainModel domainModel;
	private final PermissionManager permissions;

	public GetDomainObject(
			DomainModel domainModel,
			PermissionManager permissions) {
		this.domainModel = domainModel;
		this.permissions = permissions;
	}

	public static final class Argument {
		public String Name;
		public String[] Uri;
		public boolean MatchOrder;

		public Argument(String name, String[] uri, boolean matchOrder) {
			this.Name = name;
			this.Uri = uri;
			this.MatchOrder = matchOrder;
		}

		@SuppressWarnings("unused")
		private Argument() {
		}
	}

	@Override
	public <TInput, TOutput> CommandResult<TOutput> execute(
			ServiceLocator locator,
			Serialization<TInput> input,
			Serialization<TOutput> output,
			TInput data,
			Principal principal) {
		Argument arg;
		try {
			arg = input.deserialize(data, Argument.class);
		} catch (IOException e) {
			return CommandResult.badRequest(e.getMessage());
		}
		Optional<Class<?>> manifest = domainModel.find(arg.Name);
		if (!manifest.isPresent()) {
			return CommandResult.badRequest("Unable to find specified domain object: " + arg.Name);
		}
		if (arg.Uri == null || arg.Uri.length == 0) {
			return CommandResult.badRequest("Uri not specified.");
		}
		if (!Identifiable.class.isAssignableFrom(manifest.get())) {
			return CommandResult.badRequest("Specified type is not an identifiable: " + arg.Name);
		}
		if (!permissions.canAccess(manifest.get(), principal)) {
			return CommandResult.forbidden(arg.Name);
		}
		Repository repository;
		try {
			repository = locator.resolve(Repository.class, manifest.get());
		} catch (ReflectiveOperationException e) {
			return CommandResult.badRequest("Error resolving repository for: " + arg.Name + ". Reason: " + e.getMessage());
		}
		List<AggregateRoot> found = repository.find(arg.Uri);
		try {
			return new CommandResult<>(output.serialize(found), "Found " + found.size() + " items", 200);
		} catch (IOException e) {
			return new CommandResult<>(null, "Error serializing result.", 500);
		}
	}
}
