import { useEffect, useState } from "react";
import type { WalkthroughStep } from "../types/assistant";

type Props = {
    steps: WalkthroughStep[];
    activeIndex: number;
    onNext: () => void;
    onPrev: () => void;
    onClose: () => void;
};

type Rect = {
    top: number;
    left: number;
    width: number;
    height: number;
};

export function WalkthroughOverlay({
                                       steps,
                                       activeIndex,
                                       onNext,
                                       onPrev,
                                       onClose,
                                   }: Props) {
    const step = steps[activeIndex];
    const [rect, setRect] = useState<Rect | null>(null);

    useEffect(() => {
        if (!step) return;

        const timeoutId = window.setTimeout(() => {
            const element = document.getElementById(step.targetId);

            if (!element) {
                setRect(null);
                return;
            }

            element.scrollIntoView({
                behavior: "smooth",
                block: "center",
                inline: "center",
            });

            function updateRect() {
                const el = document.getElementById(step.targetId);
                if (!el) return;

                const bounds = el.getBoundingClientRect();

                setRect({
                    top: bounds.top,
                    left: bounds.left,
                    width: bounds.width,
                    height: bounds.height,
                });
            }

            requestAnimationFrame(updateRect);

            window.addEventListener("resize", updateRect);
            window.addEventListener("scroll", updateRect, true);
        }, 120);

        return () => {
            window.clearTimeout(timeoutId);
            window.removeEventListener("resize", () => {});
            window.removeEventListener("scroll", () => {}, true);
        };
    }, [step]);

    if (!step) return null;

    const controlsStyle = rect
        ? {
            top:
                rect.top + rect.height + 12 > window.innerHeight - 60
                    ? rect.top - 58
                    : rect.top + rect.height + 12,
            left: Math.min(Math.max(rect.left, 16), window.innerWidth - 260),
        }
        : undefined;

    const cardStyle = rect
        ? {
            top:
                rect.top + rect.height + 82 > window.innerHeight
                    ? Math.max(rect.top - 190, 24)
                    : rect.top + rect.height + 70,
            left: Math.min(Math.max(rect.left, 16), window.innerWidth - 440),
        }
        : undefined;

    return (
        <div className="walkthrough-layer">
            {rect && (
                <>
                    <div
                        className="walkthrough-highlight"
                        style={{
                            top: rect.top - 8,
                            left: rect.left - 8,
                            width: rect.width + 16,
                            height: rect.height + 16,
                        }}
                    />

                    <div className="walkthrough-mini-controls" style={controlsStyle}>
                        <button
                            type="button"
                            className="walkthrough-control-button"
                            onClick={onPrev}
                            disabled={activeIndex === 0}
                        >
                            Prev
                        </button>

                        <button
                            type="button"
                            className="walkthrough-control-button primary-control"
                            onClick={onNext}
                        >
                            {activeIndex === steps.length - 1 ? "Finish" : "Next"}
                        </button>

                        <button
                            type="button"
                            className="walkthrough-control-button"
                            onClick={onClose}
                        >
                            Skip
                        </button>
                    </div>
                </>
            )}

            <div className="walkthrough-card floating-card" style={cardStyle}>
                <div className="walkthrough-kicker">
                    Step {activeIndex + 1} / {steps.length}
                </div>

                <h3>{step.title}</h3>
                <p>{step.description}</p>
            </div>
        </div>
    );
}