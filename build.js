const ROOT = build({
	module: "com.s8.core.db.cobalt",
	dependencies: [
		"S8-api",
		"S8-core-io-xml",
		"S8-core-io-JSON",
		"S8-core-io-bytes",
		"S8-core-bohr-atom",
		"S8-core-bohr-lithium",
		"S8-core-arch-silicon",
		"S8-core-arch-titanium",
	],
	target: "S8-core-db-cobalt"
});
