insert into colors (id, code) values (0, 'Black');
insert into colors (id, code) values (1, 'White');
insert into colors (id, code) values (2, 'Yellow');

insert into phones (id, brand, model, price, displaySizeInches, weightGr, lengthMm, widthMm, heightMm, announced, deviceType, os, displayResolution, pixelDensity, displayTechnology, backCameraMegapixels, frontCameraMegapixels, ramGb, internalStorageGb, batteryCapacityMah, talkTimeHours, standByTimeHours, bluetooth, positioning, imageUrl, description) values (1000, 'test', 'test', 1.0, 10.1, 482, 276.0, 167.0, 12.6, null, 'test', 'test', 'test', 149, null, null, 1.3, null, 8.0, null, null, null, 'test', 'test', 'test', 'test');
insert into phones (id, brand, model, price, displaySizeInches, weightGr, lengthMm, widthMm, heightMm, announced, deviceType, os, displayResolution, pixelDensity, displayTechnology, backCameraMegapixels, frontCameraMegapixels, ramGb, internalStorageGb, batteryCapacityMah, talkTimeHours, standByTimeHours, bluetooth, positioning, imageUrl, description) values ('1001', 'test', 'test1', 1.0, 10.1, 482, 250.0, 174.0, 10.0, null, 'test', 'test', 'test', 149, 'test', 5.0, 0.3, 1.0, 8.0, 6500, null, null, 'test', 'test', 'test', 'test');
insert into phones (id, brand, model, price, displaySizeInches, weightGr, lengthMm, widthMm, heightMm, announced, deviceType, os, displayResolution, pixelDensity, displayTechnology, backCameraMegapixels, frontCameraMegapixels, ramGb, internalStorageGb, batteryCapacityMah, talkTimeHours, standByTimeHours, bluetooth, positioning, imageUrl, description) values ('1002', 'test', 'test2', 1.0, 10.1, 482, null, null, null, null, 'test', 'test', 'test', 118, 'test', null, 0.3, null, 8.0, null, null, null, 'test', null, 'test', 'test');
insert into phones (id, brand, model, price, displaySizeInches, weightGr, lengthMm, widthMm, heightMm, announced, deviceType, os, displayResolution, pixelDensity, displayTechnology, backCameraMegapixels, frontCameraMegapixels, ramGb, internalStorageGb, batteryCapacityMah, talkTimeHours, standByTimeHours, bluetooth, positioning, imageUrl, description) values ('1003', 'test', 'test3', 249, 10.1, 482, 240.0, 172.0, 10.0, null, 'test', 'test', 'test', 224, 'test', 5.0, 2.0, 2.0, 16.0, 7000, null, null, 'test', 'test', 'test', 'test');
insert into phones (id, brand, model, price, displaySizeInches, weightGr, lengthMm, widthMm, heightMm, announced, deviceType, os, displayResolution, pixelDensity, displayTechnology, backCameraMegapixels, frontCameraMegapixels, ramGb, internalStorageGb, batteryCapacityMah, talkTimeHours, standByTimeHours, bluetooth, positioning, imageUrl, description) values ('1004', 'test', 'test4', 1.0, 10.1, 482, 265.4, 181.0, 13.4, null, 'test', 'test', 'test', 149, 'test', 5.0, 2.0, 1.0, 16.0, 6000, null, null, 'test', 'test', 'test', 'test');
insert into phones (id, brand, model, price, displaySizeInches, weightGr, lengthMm, widthMm, heightMm, announced, deviceType, os, displayResolution, pixelDensity, displayTechnology, backCameraMegapixels, frontCameraMegapixels, ramGb, internalStorageGb, batteryCapacityMah, talkTimeHours, standByTimeHours, bluetooth, positioning, imageUrl, description) values ('1005', 'test', 'test5', 1.0, 10.1, 482, 263.0, 174.0, 10.5, '2013-01-10 00:00:00', 'test', 'test', 'test', 149, 'test', 2.0, null, 1.0, 8.0, null, null, null, 'test', null, 'test', 'test');

insert into phone2color (phoneId, colorId) values (1000, 0);
insert into phone2color (phoneId, colorId) values (1001, 1);
insert into phone2color (phoneId, colorId) values (1002, 0);

insert into stocks (phoneId, stock, reserved) values (1000, 11, 7);
insert into stocks (phoneId, stock, reserved) values (1001, 12, 8);
insert into stocks (phoneId, stock, reserved) values (1002, 13, 9);
insert into stocks (phoneId, stock, reserved) values (1003, 14, 10);
insert into stocks (phoneId, stock, reserved) values (1004, 15, 0);
