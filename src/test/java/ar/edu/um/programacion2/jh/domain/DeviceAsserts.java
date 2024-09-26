package ar.edu.um.programacion2.jh.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class DeviceAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDeviceAllPropertiesEquals(Device expected, Device actual) {
        assertDeviceAutoGeneratedPropertiesEquals(expected, actual);
        assertDeviceAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDeviceAllUpdatablePropertiesEquals(Device expected, Device actual) {
        assertDeviceUpdatableFieldsEquals(expected, actual);
        assertDeviceUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDeviceAutoGeneratedPropertiesEquals(Device expected, Device actual) {
        assertThat(expected)
            .as("Verify Device auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDeviceUpdatableFieldsEquals(Device expected, Device actual) {
        assertThat(expected)
            .as("Verify Device relevant properties")
            .satisfies(e -> assertThat(e.getCode()).as("check code").isEqualTo(actual.getCode()))
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()))
            .satisfies(e -> assertThat(e.getDescription()).as("check description").isEqualTo(actual.getDescription()))
            .satisfies(e -> assertThat(e.getBasePrice()).as("check basePrice").isEqualTo(actual.getBasePrice()))
            .satisfies(e -> assertThat(e.getCurrency()).as("check currency").isEqualTo(actual.getCurrency()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDeviceUpdatableRelationshipsEquals(Device expected, Device actual) {
        assertThat(expected)
            .as("Verify Device relationships")
            .satisfies(e -> assertThat(e.getCharacteristics()).as("check characteristics").isEqualTo(actual.getCharacteristics()))
            .satisfies(e -> assertThat(e.getOptions()).as("check options").isEqualTo(actual.getOptions()))
            .satisfies(e -> assertThat(e.getExtras()).as("check extras").isEqualTo(actual.getExtras()));
    }
}
