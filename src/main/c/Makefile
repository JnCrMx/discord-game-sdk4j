CC=gcc
CFLAGS=-fPIC -c -I"${JAVA_HOME}/include" -I"${JAVA_HOME}/include/win32" -I"../../../discord_game_sdk/c"
LFLAGS=-shared -L../../../ -ldiscord_game_sdk
OBJ = \
	../../../target/obj/de_jcm_discordgamesdk_activity_Activity.o \
	../../../target/obj/de_jcm_discordgamesdk_activity_ActivityAssets.o \
	../../../target/obj/de_jcm_discordgamesdk_activity_ActivityParty.o \
	../../../target/obj/de_jcm_discordgamesdk_activity_ActivityPartySize.o \
	../../../target/obj/de_jcm_discordgamesdk_activity_ActivitySecrets.o \
	../../../target/obj/de_jcm_discordgamesdk_activity_ActivityTimestamps.o \
	../../../target/obj/de_jcm_discordgamesdk_ActivityManager.o \
	../../../target/obj/de_jcm_discordgamesdk_CreateParams.o \
	../../../target/obj/de_jcm_discordgamesdk_Core.o \

../../../target/obj/%.o: %.c
	$(CC) -c -o $@ $< $(CFLAGS)

../../../native.dll: $(OBJ)
	$(CC) -o $@ $^ $(LFLAGS)

.PHONY: clean

clean:
	rm ../../../target/obj/*.o
	rm ../../../native.dll
