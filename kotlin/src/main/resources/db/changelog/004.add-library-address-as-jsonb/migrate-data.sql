UPDATE LIBRARY
SET ADDRESS = jsonb_build_object(
  'line1', LIBRARY.ADDRESS_LINE_1,
  'line2', LIBRARY.ADDRESS_LINE_2,
  'postalCode', LIBRARY.POSTAL_CODE,
  'city', LIBRARY.CITY
)
WHERE ADDRESS = '{}'::jsonb;
