plugins {
    id("dev.deftu.gradle.multiversion-root")
}

preprocess {
    strictExtraMappings.set(false)

    "1.21.8-neoforge"(1_21_08, "srg") {
        "1.21.8-fabric"(1_21_08, "yarn") {
            "1.21.7-fabric"(1_21_07, "yarn") {
                "1.21.7-neoforge"(1_21_07, "srg") {
                    "1.21.6-neoforge"(1_21_06, "srg") {
                        "1.21.6-fabric"(1_21_06, "yarn") {
                            "1.21.5-fabric"(1_21_05, "yarn") {
                                "1.21.5-neoforge"(1_21_05, "srg") {
                                    "1.21.4-neoforge"(1_21_04, "srg") {
                                        "1.21.4-fabric"(1_21_04, "yarn") {
                                            "1.21.3-fabric"(1_21_04, "yarn") {
                                                "1.21.3-neoforge"(1_21_03, "srg") {
                                                    "1.21.2-neoforge"(1_21_02, "srg") {
                                                        "1.21.2-fabric"(1_21_02, "yarn") {
                                                            "1.21.1-fabric"(1_21_01, "yarn") {
                                                                "1.21.1-neoforge"(1_21_01, "srg") {
                                                                    "1.20.6-neoforge"(1_20_06, "srg") {
                                                                        "1.20.6-fabric"(1_20_06, "yarn") {
                                                                            "1.20.4-fabric"(1_20_04, "yarn") {
                                                                                "1.20.4-neoforge"(1_20_04, "srg") {
                                                                                    "1.20.4-forge"(1_20_04, "srg") {
                                                                                        "1.20.1-forge"(1_20_01, "srg") {
                                                                                            "1.20.1-fabric"(1_20_01, "yarn") {
                                                                                                "1.19.4-fabric"(1_19_04, "yarn") {
                                                                                                    "1.19.4-forge"(1_19_04, "srg")
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
