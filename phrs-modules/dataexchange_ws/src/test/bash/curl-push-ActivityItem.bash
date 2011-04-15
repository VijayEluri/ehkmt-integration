#!/bin/bash
#set -xv

HOST=http://localhost:8080
CONTEXT=dataexchange_ws/dynamic_bean_repository
# phrsBeanClassURI : at.srfg.kmt.ehealth.phrs.datamodel.impl.ActivityItem
curl --fail -X POST --data 'dynaBean={"class":"at.srfg.kmt.ehealth.phrs.presentation.model.observation.ActivityItem","id":null,"_phrsBeanCanRead":true,"_phrsBeanCanUse":true,"_phrsBeanCanWrite":true,"_phrsBeanClassURI":"at.srfg.kmt.ehealth.phrs.datamodel.impl.ActivityItem","_phrsBeanCreateDate":"2011-04-14T15:36:07Z","_phrsBeanCreatorUri":null,"_phrsBeanIsDeleted":false,"_phrsBeanName":null,"_phrsBeanOwnerUri":null,"_phrsBeanRefersToSourceUri":null,"_phrsBeanUri":"phrs_uuid_df6ab7ef-6175-443b-a7a1-a647d026e4a0","activityCategory":null,"activityCode":"phf:jogging","activityDurationCode":"phf:15_min","activityFeature":{},"activityFrequencyCode":"phf:Every_Day","activityName":"other stuff","activityShortComment":null,"assessmentIndicator":null,"comment":null,"isActiveStatus":true,"moodIndicator":null,"observationDateEnd":"2011-04-14T15:34:00Z","observationDateStart":"2011-04-14T15:34:00Z","ownerUser":{"class":"User","id":2}}' $HOST/$CONTEXT/persist
 